# FinanciaPro
Plateforme collaborative de gestion budgétaire et de microcrédits entre particuliers

---

## Atelier : Services Web - Version 1
**PROMOTION 2024 - 2025**  
**Date:** 03/06/2025  
**Formateur:** DANGLA Loïc  

---
## Objectif

**FinanciaPro** vise à :

- **Concevoir une API REST modulaire** avec plusieurs entités liées
- **Implémenter une authentification** API Key ou Basic (voire JWT en bonus)
- **Mettre en œuvre des calculs métier non triviaux** (intérêts, équilibrage de dettes, alertes)
- **Utiliser DTOs et Mapping** (pour ne pas exposer les entités directement)
- **Structurer un projet Spring Boot complet** avec tests via Postman
- **Préparer une vraie documentation** (Swagger)

---
## Contexte

Vous développez un back-end pour une plateforme entre particuliers qui permet :

- De gérer son budget (revenus / dépenses / prévisionnel)
- De faire des prêts/remboursements entre utilisateurs
- De suivre l’historique et les engagements en cours
- De simuler l’évolution de son budget sur 3 mois

---
## Modèle de données

### User

- **Id**
- **Prénom**
- **Nom**
- **Email**
- **ApiKey**

### BudgetItem

- **Id**
- **type (INCOME / EXPENSE)**
- **Montant**
- **Description**
- **Date**
- **User**
### LoanRequest

- **Id**
- **Borrower** (Emprunteur)
- **Lender** (Prêteur)
- **Montant**
- **Intérêt**
- **Durée** (mois)
- **Statut** (PENDING / ACCEPTED / REFUSED)

### Repayment

- **Id**
- **loanRequest** (Référence à la demande de prêt)
- **Montant**
- **Date**
- **Commentaire**

---
## Relation clés

- Un User peut avoir plusieurs BudgetItem
- Un User peut créer ou accepter un LoanRequest
- Un LoanRequest accepté est suivi par des Repayment

---
## Endpoints

/users
- POST /register
- GET /me (avec clé API)
- GET /summary → total revenus/dépenses/solde

/budget
- GET / (filtrage par date, type, montant)
- POST /add
- DELETE /{id}

/loans
- POST /request → créer une demande
- GET /incoming → demandes reçues
- PUT /{id}/accept / PUT /{id}/refuse
- GET /history

/repayments
- POST /loan/{id}/repay
- GET /loan/{id}/repayments

---
## Logiques métiers

Logiques métiers complexes à intégrer :

- Le solde global = revenus - dépenses + prêts reçus - prêts accordés non remboursés
- Simulation sur 3 mois : en ajoutant revenus/dépenses prévisionnels et remboursements
- Empêcher l’acceptation de prêt si le prêteur a moins de 500€ de solde
- Alertes (console/json) si un utilisateur est en situation "crédit > revenus x 2"
---
## Authentification

**FinanciaPro** utilise un système d'authentification par clé API :

- Chaque utilisateur a une clé API (stockée dans la base) qu'il doit transmettre dans les headers
- **Exemple d'utilisation :**
```http
  GET /me
  Authorization: ApiKey abcdef123456
```
---