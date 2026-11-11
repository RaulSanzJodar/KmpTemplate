-- Perfil de cada usuario. Patrón a copiar para cualquier tabla nueva:
-- 1) clave que referencia a auth.users, 2) RLS activado, 3) políticas explícitas por operación.

create table public.profiles (
    id uuid primary key references auth.users (id) on delete cascade,
    name text,
    email text,
    created_at timestamptz not null default now()
);

alter table public.profiles enable row level security;

-- `(select auth.uid())` en lugar de `auth.uid()`: Postgres lo evalúa una vez por consulta y no por fila.
create policy "Cada usuario lee su perfil"
    on public.profiles for select
    to authenticated
    using ((select auth.uid()) = id);

create policy "Cada usuario edita su perfil"
    on public.profiles for update
    to authenticated
    using ((select auth.uid()) = id)
    with check ((select auth.uid()) = id);

-- Crea el perfil al registrarse el usuario (correo, Google o Apple).
create function public.handle_new_user()
returns trigger
language plpgsql
security definer
set search_path = ''
as $$
begin
    insert into public.profiles (id, name, email)
    values (
        new.id,
        coalesce(new.raw_user_meta_data ->> 'full_name', new.raw_user_meta_data ->> 'name'),
        new.email
    );
    return new;
end;
$$;

-- La función solo debe ejecutarla el trigger, no la API.
revoke execute on function public.handle_new_user() from public, anon, authenticated;

create trigger on_auth_user_created
    after insert on auth.users
    for each row execute function public.handle_new_user();
