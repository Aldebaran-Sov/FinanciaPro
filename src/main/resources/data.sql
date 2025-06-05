-- ==================== INSERTION DES UTILISATEURS ====================
INSERT INTO USERS (ID, NOM, PRENOM, EMAIL, API_KEY) VALUES
                                                        (1, 'Dupont', 'Jean', 'jean.dupont@email.com', 'api_key_jean_123456'),
                                                        (2, 'Martin', 'Marie', 'marie.martin@email.com', 'api_key_marie_789012'),
                                                        (3, 'Durand', 'Pierre', 'pierre.durand@email.com', 'api_key_pierre_345678'),
                                                        (4, 'Moreau', 'Sophie', 'sophie.moreau@email.com', 'api_key_sophie_901234'),
                                                        (5, 'Lefebvre', 'Antoine', 'antoine.lefebvre@email.com', 'api_key_antoine_567890');

-- ==================== INSERTION DES BUDGET ITEMS ====================
INSERT INTO BUDGET_ITEMS (ID, DESCRIPTION, MONTANT, DATE, TYPE, USER_ID) VALUES
                                                                             (1, 'Salaire Janvier', 2500.00, '2024-01-01', 'INCOME', 1),
                                                                             (2, 'Loyer', -800.00, '2024-01-01', 'EXPENSE', 1),
                                                                             (3, 'Courses alimentaires', -150.00, '2024-01-05', 'EXPENSE', 1),
                                                                             (4, 'Facture électricité', -80.00, '2024-01-10', 'EXPENSE', 1),
                                                                             (5, 'Freelance', 500.00, '2024-01-15', 'INCOME', 1),
                                                                             (6, 'Salaire Janvier', 3000.00, '2024-01-01', 'INCOME', 2),
                                                                             (7, 'Loyer', -1200.00, '2024-01-01', 'EXPENSE', 2),
                                                                             (8, 'Transport', -120.00, '2024-01-03', 'EXPENSE', 2),
                                                                             (9, 'Restaurant', -80.00, '2024-01-07', 'EXPENSE', 2),
                                                                             (10, 'Prime', 800.00, '2024-01-20', 'INCOME', 2);

-- ==================== INSERTION DES LOAN REQUESTS ====================
INSERT INTO LOAN_REQUESTS (ID, BORROWER_ID, LENDER_ID, MONTANT, TAUX_INTERET, DUREE_EN_MOIS, COMMENTAIRE, DATE_CREATION, DATE_ACCEPTATION, STATUT) VALUES
                                                                                                                                                       (1, 1, 2, 1000.00, 3.50, 12, 'Réparation voiture urgente', '2024-01-01', '2024-01-02', 'ACCEPTED'),
                                                                                                                                                       (2, 3, NULL, 500.00, 2.00, 6, 'Achat équipement informatique', '2024-01-15', NULL, 'PENDING'),
                                                                                                                                                       (3, 1, 4, 750.00, 4.00, 8, 'Frais de formation', '2024-01-10', '2024-01-12', 'REFUSED'),
                                                                                                                                                       (4, 2, 3, 300.00, 1.50, 4, 'Dépannage temporaire', '2024-01-20', '2024-01-21', 'ACCEPTED');

-- ==================== INSERTION DES REPAYMENTS ====================
INSERT INTO REPAYMENT (ID, LOAN_REQUEST_ID, MONTANT, DATE, COMMENTAIRE) VALUES
                                                                            (1, 1, 90.00, '2024-02-01', 'Premier remboursement - Virement'),
                                                                            (2, 1, 90.00, '2024-03-01', 'Deuxième remboursement - Virement'),
                                                                            (3, 1, 90.00, '2024-04-01', 'Troisième remboursement - Chèque'),
                                                                            (4, 4, 80.00, '2024-02-20', 'Premier remboursement - Espèces');
