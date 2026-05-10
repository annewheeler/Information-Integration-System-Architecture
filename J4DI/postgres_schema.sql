--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: companies_pg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.companies_pg (
    company_id character varying(30) NOT NULL,
    industry character varying(100),
    country character varying(100),
    company_size character varying(50)
);


ALTER TABLE public.companies_pg OWNER TO postgres;

--
-- Name: company_ai_adoption_pg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_ai_adoption_pg (
    response_id integer NOT NULL,
    company_id character varying(30) NOT NULL,
    survey_year integer,
    quarter character varying(10),
    ai_adoption_rate numeric(10,2),
    ai_adoption_stage character varying(50),
    years_using_ai integer,
    ai_primary_tool character varying(100),
    num_ai_tools_used integer,
    ai_use_case character varying(100),
    ai_projects_active integer
);


ALTER TABLE public.company_ai_adoption_pg OWNER TO postgres;

--
-- Name: company_ai_governance_pg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_ai_governance_pg (
    response_id integer NOT NULL,
    company_id character varying(30) NOT NULL,
    survey_year integer,
    quarter character varying(10),
    ai_training_hours numeric(10,2),
    ai_maturity_score numeric(10,3),
    ai_failure_rate numeric(10,2),
    ai_risk_management_score integer
);


ALTER TABLE public.company_ai_governance_pg OWNER TO postgres;

--
-- Name: companies_pg companies_pg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.companies_pg
    ADD CONSTRAINT companies_pg_pkey PRIMARY KEY (company_id);


--
-- Name: company_ai_adoption_pg company_ai_adoption_pg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_ai_adoption_pg
    ADD CONSTRAINT company_ai_adoption_pg_pkey PRIMARY KEY (response_id);


--
-- Name: company_ai_governance_pg company_ai_governance_pg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_ai_governance_pg
    ADD CONSTRAINT company_ai_governance_pg_pkey PRIMARY KEY (response_id);


--
-- Name: company_ai_adoption_pg fk_company_ai_adoption_pg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_ai_adoption_pg
    ADD CONSTRAINT fk_company_ai_adoption_pg FOREIGN KEY (company_id) REFERENCES public.companies_pg(company_id);


--
-- Name: company_ai_governance_pg fk_company_ai_governance_pg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_ai_governance_pg
    ADD CONSTRAINT fk_company_ai_governance_pg FOREIGN KEY (company_id) REFERENCES public.companies_pg(company_id);


--
-- Name: TABLE companies_pg; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.companies_pg TO web_anon;


--
-- Name: TABLE company_ai_adoption_pg; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.company_ai_adoption_pg TO web_anon;


--
-- Name: TABLE company_ai_governance_pg; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.company_ai_governance_pg TO web_anon;


--
-- PostgreSQL database dump complete
--

