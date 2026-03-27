# Information-Integration-System-Architecture
## Data Source Design

The original dataset ( https://www.kaggle.com/datasets/mohankrishnathalla/global-ai-adoption-and-workforce-impact-dataset?select=ai_company_adoption.csv) 
was decomposed into multiple independent data sources, each representing a different business domain.

- **PostgreSQL** – AI operations data (adoption, tools, use cases, maturity)
- **Oracle** – workforce and business impact (productivity, jobs, performance)
- **MongoDB** – country and industry benchmarks (JSON hierarchical documents)
- **Excel** – aggregated reporting data (country-level summaries)

The decomposition ensures:
- separation of concerns
- minimal data redundancy
- compatibility with data integration scenarios

Key integration fields:
- `response_id` – links observations across relational sources
- `company_id` – company-level integration
- `country`, `industry` – contextual joins
- `survey_year`, `quarter` – time-based analysis
