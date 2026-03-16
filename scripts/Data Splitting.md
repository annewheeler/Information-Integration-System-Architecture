

import pandas as pd
import json

# 1. Încărcăm dataset-ul original
df = pd.read_csv('WA_Fn-UseC_-HR-Employee-Attrition.csv')

# 2. Sursa POSTGRESQL: Date de identitate și profil (SQL)
# Demonstrăm "Format Matching" prin selectarea coloanelor de bază [cite: 2408]
pg_data = df[['EmployeeNumber', 'Age', 'Gender', 'MaritalStatus', 'EducationField', 'Department']]
pg_data.to_csv('01_HR_Identity_PG.csv', index=False)

# 3. Sursa ORACLE: Date financiare și performanță (Arhitectura FDB centrală) [cite: 172, 306]
oracle_data = df[['EmployeeNumber', 'MonthlyIncome', 'PercentSalaryHike', 'StockOptionLevel', 'TrainingTimesLastYear']]
oracle_data.to_csv('02_HR_Finance_ORCL.csv', index=False)

# 4. Sursa MONGODB: Feedback calitativ (Format JSON) [cite: 306, 791]
# Transformăm coloanele de satisfacție într-o listă de dicționare (Document-based)
mongo_cols = ['EmployeeNumber', 'JobSatisfaction', 'RelationshipSatisfaction', 'WorkLifeBalance', 'EnvironmentSatisfaction']
mongo_data = df[mongo_cols].to_dict(orient='records')
with open('03_HR_Feedback_MONGO.json', 'w') as f:
    json.dump(mongo_data, f, indent=4)

# 5. Sursa EXCEL: Nomenclator Locații/Roluri (Document.DS) [cite: 23, 1247]
# Creăm un set mic de date pentru referință transversală
excel_data = df[['Department', 'JobRole', 'BusinessTravel']].drop_duplicates()
excel_data.to_excel('04_HR_Reference_DATA.xlsx', index=False)

print("Succes! Cele 4 surse eterogene au fost create.")
