PGDMP  2    '        
         }            WeatherData    17.2    17.2 0    |           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            }           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            ~           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    16476    WeatherData    DATABASE     �   CREATE DATABASE "WeatherData" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "WeatherData";
                     postgres    false                        3079    16526    postgis 	   EXTENSION     ;   CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;
    DROP EXTENSION postgis;
                        false            �           0    0    EXTENSION postgis    COMMENT     ^   COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';
                             false    2            S           1247    16520    ROLE    TYPE     ?   CREATE TYPE public."ROLE" AS ENUM (
    'USER',
    'ADMIN'
);
    DROP TYPE public."ROLE";
       public               postgres    false            Q           1255    17810 
   set_geom()    FUNCTION       CREATE FUNCTION public.set_geom() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Automatically set the 'geom' field using longitude and latitude
    NEW.geom = ST_SetSRID(ST_MakePoint(NEW.longitude, NEW.latitude), 4326);
    RETURN NEW;
END;
$$;
 !   DROP FUNCTION public.set_geom();
       public               postgres    false            �            1259    17614    location    TABLE     �   CREATE TABLE public.location (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    weather_data json NOT NULL,
    geom public.geometry NOT NULL
);
    DROP TABLE public.location;
       public         heap r       postgres    false    2    2    2    2    2    2    2    2            �            1259    17613    Location_id_seq    SEQUENCE     z   CREATE SEQUENCE public."Location_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public."Location_id_seq";
       public               postgres    false    229            �           0    0    Location_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public."Location_id_seq" OWNED BY public.location.id;
          public               postgres    false    228            �            1259    17774    location_id_seq    SEQUENCE     x   CREATE SEQUENCE public.location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.location_id_seq;
       public               postgres    false            �            1259    16496    role    TABLE     W   CREATE TABLE public.role (
    id integer NOT NULL,
    role public."ROLE" NOT NULL
);
    DROP TABLE public.role;
       public         heap r       postgres    false    1619            �            1259    16495    role_id_seq    SEQUENCE     �   CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.role_id_seq;
       public               postgres    false    221            �           0    0    role_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;
          public               postgres    false    220            �            1259    16481    user    TABLE     �   CREATE TABLE public."user" (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role_id integer NOT NULL,
    location_id integer NOT NULL
);
    DROP TABLE public."user";
       public         heap r       postgres    false            �            1259    16480    user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.user_id_seq;
       public               postgres    false    219            �           0    0    user_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;
          public               postgres    false    218            �            1259    16504 	   user_role    TABLE     w   CREATE TABLE public.user_role (
    id integer NOT NULL,
    user_id integer NOT NULL,
    role_id integer NOT NULL
);
    DROP TABLE public.user_role;
       public         heap r       postgres    false            �            1259    17660    user_role_id_seq    SEQUENCE     �   CREATE SEQUENCE public.user_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.user_role_id_seq;
       public               postgres    false    222            �           0    0    user_role_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.user_role_id_seq OWNED BY public.user_role.id;
          public               postgres    false    230            �           2604    17617    location id    DEFAULT     l   ALTER TABLE ONLY public.location ALTER COLUMN id SET DEFAULT nextval('public."Location_id_seq"'::regclass);
 :   ALTER TABLE public.location ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    228    229    229            �           2604    16499    role id    DEFAULT     b   ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);
 6   ALTER TABLE public.role ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    221    221            �           2604    16484    user id    DEFAULT     d   ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);
 8   ALTER TABLE public."user" ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    219    218    219            �           2604    17661    user_role id    DEFAULT     l   ALTER TABLE ONLY public.user_role ALTER COLUMN id SET DEFAULT nextval('public.user_role_id_seq'::regclass);
 ;   ALTER TABLE public.user_role ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    230    222            w          0    17614    location 
   TABLE DATA           U   COPY public.location (id, name, latitude, longitude, weather_data, geom) FROM stdin;
    public               postgres    false    229   +5       t          0    16496    role 
   TABLE DATA           (   COPY public.role (id, role) FROM stdin;
    public               postgres    false    221   �6       �          0    16848    spatial_ref_sys 
   TABLE DATA           X   COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
    public               postgres    false    224   7       r          0    16481    user 
   TABLE DATA           J   COPY public."user" (id, name, password, role_id, location_id) FROM stdin;
    public               postgres    false    219    7       u          0    16504 	   user_role 
   TABLE DATA           9   COPY public.user_role (id, user_id, role_id) FROM stdin;
    public               postgres    false    222   m7       �           0    0    Location_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public."Location_id_seq"', 10, true);
          public               postgres    false    228            �           0    0    location_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.location_id_seq', 116, true);
          public               postgres    false    231            �           0    0    role_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.role_id_seq', 188, true);
          public               postgres    false    220            �           0    0    user_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.user_id_seq', 7, true);
          public               postgres    false    218            �           0    0    user_role_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.user_role_id_seq', 1, false);
          public               postgres    false    230            �           2606    17621    location Location_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.location
    ADD CONSTRAINT "Location_pkey" PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.location DROP CONSTRAINT "Location_pkey";
       public                 postgres    false    229            �           2606    17623    location loc_name_uniq 
   CONSTRAINT     `   ALTER TABLE ONLY public.location
    ADD CONSTRAINT loc_name_uniq UNIQUE (name) INCLUDE (name);
 @   ALTER TABLE ONLY public.location DROP CONSTRAINT loc_name_uniq;
       public                 postgres    false    229            �           2606    16501    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public                 postgres    false    221            �           2606    16503    user user_name_uniq 
   CONSTRAINT     P   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_name_uniq UNIQUE (name);
 ?   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_name_uniq;
       public                 postgres    false    219            �           2606    16488    user user_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_pkey;
       public                 postgres    false    219            �           2606    17666    user_role user_role_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.user_role DROP CONSTRAINT user_role_pkey;
       public                 postgres    false    222            �           2620    17811    location trig_set_geom    TRIGGER     y   CREATE TRIGGER trig_set_geom BEFORE INSERT OR UPDATE ON public.location FOR EACH ROW EXECUTE FUNCTION public.set_geom();
 /   DROP TRIGGER trig_set_geom ON public.location;
       public               postgres    false    229    337            �           2606    17684    user FK_user_location_id    FK CONSTRAINT     �   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT "FK_user_location_id" FOREIGN KEY (location_id) REFERENCES public.location(id);
 F   ALTER TABLE ONLY public."user" DROP CONSTRAINT "FK_user_location_id";
       public               postgres    false    219    5585    229            �           2606    17694    user_role fk_user_role_role_id    FK CONSTRAINT     |   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES public.role(id);
 H   ALTER TABLE ONLY public.user_role DROP CONSTRAINT fk_user_role_role_id;
       public               postgres    false    222    5579    221            �           2606    17689    user_role fk_user_role_user_id    FK CONSTRAINT     ~   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES public."user"(id);
 H   ALTER TABLE ONLY public.user_role DROP CONSTRAINT fk_user_role_user_id;
       public               postgres    false    219    222    5577            �           2606    17672    user_role role_id_fk    FK CONSTRAINT     |   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES public.role(id) NOT VALID;
 >   ALTER TABLE ONLY public.user_role DROP CONSTRAINT role_id_fk;
       public               postgres    false    5579    221    222            �           2606    17667    user_role user_id_fk    FK CONSTRAINT     ~   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id) NOT VALID;
 >   ALTER TABLE ONLY public.user_role DROP CONSTRAINT user_id_fk;
       public               postgres    false    219    222    5577            �           2606    17679    user user_location_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_location_fk FOREIGN KEY (location_id) REFERENCES public.location(id) NOT VALID;
 A   ALTER TABLE ONLY public."user" DROP CONSTRAINT user_location_fk;
       public               postgres    false    229    219    5585            w   �  x���K��@ �s�+�ϣT������@�Cr
I���ȸ��`���l ����R}�)(��*�q�¬�X�1()Y$��%&�]�qym�v��}��.䍟�}�ۡ�����p����͏n^FOyao
	�~� 
+K����J9T�s�M���Q ��c[��������E��Z�v-��(��ai�(�EQ*�������*�RԪD�(6�ifB��*�Cj�R�[���N'R�Bmtv�����I����d���P����a�W?`I����?L��tV�@���͏�M��"��ɟ�/�xb�Ҳ���¯�����~�Ѹ����S�|����^y�H�%H��ݾ�ND�������?����o��Jy_	�X��n���m�3YV�Bb����3U�!�`�B�W�[�O�m      t      x�3�tt����2�v����� +��      �      x������ � �      r   =   x�3�,I-.���F\���y%�F#NC.3���� �$#�U���7����� ��n      u      x������ � �     