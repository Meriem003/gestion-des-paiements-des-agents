# Système de Gestion des Paiements des Agents

Ce système permet la gestion complète des paiements des agents d'une entreprise avec différents niveaux d'accès selon le type d'utilisateur (Agent, Responsable de Département, Directeur). Il offre une interface console intuitive pour la gestion des salaires, primes, bonus et indemnités.

## ✨ Fonctionnalités

### 🔐 Authentification
- Système de connexion sécurisé
- Gestion des sessions utilisateur
- Contrôle d'accès basé sur les rôles

### 👥 Gestion des Agents
- Création et modification des profils agents
- Assignation aux départements
- Gestion des types d'agents (Ouvrier, Responsable, Directeur, Stagiaire)

### 💰 Gestion des Paiements
- **Types de paiements** : Salaire, Prime, Bonus, Indemnité
- **Validation des paiements** par les responsables
- **Historique complet** des transactions
- **Rapports de paiements** par période

### 🏢 Gestion des Départements
- Création et gestion des départements
- Attribution des responsables de département
- Vue d'ensemble des agents par département

## 🏗️ Architecture

Le projet suit le pattern **MVC (Model-View-Controller)** avec une architecture en couches :

```
📁 Couche Présentation (View)
├── MenuAgent.java
├── MenuResponsable.java
└── MenuDirecteur.java

📁 Couche Contrôleur (Controller)
├── AuthController.java
├── AgentController.java
├── PaiementController.java
├── ResponsableController.java
└── DirecteurController.java

📁 Couche Service (Business Logic)
├── AgentServiceImpl.java
├── PaiementServiceImpl.java
├── ResponsableServiceImpl.java
├── DirecteurServiceImpl.java
└── LoginService.java

📁 Couche Accès aux Données (DAO)
├── AgentDao.java
├── PaiementDao.java
└── DepartementDao.java

📁 Couche Modèle (Model)
├── Agent.java
├── Paiement.java
├── Departement.java
├── Personne.java
├── TypeAgent.java
└── TypePaiement.java
```

## 🖥️ Utilisation

### Démarrage de l'application
L'application démarre avec un écran de connexion console où vous devez saisir :
1. Email
2. Mot de passe

### Interfaces par type d'utilisateur

#### 👨‍💼 Interface Directeur
- Gestion complète des agents
- Validation des paiements
- Création de départements
- Rapports globaux

#### 👨‍💻 Interface Responsable de Département
- Gestion des agents de son département
- Validation des paiements de son équipe
- Attribution de primes et bonus

#### 👤 Interface Agent
- Consultation de ses paiements
- Historique personnel
- Informations de profil

## 📁 Structure du Projet

```
gestion-des-agents/
├── 📁 src/                     # Code source
│   ├── 📁 controller/          # Contrôleurs
│   ├── 📁 dao/                 # Accès aux données
│   ├── 📁 model/               # Modèles de données
│   ├── 📁 service/             # Logique métier
│   ├── 📁 utils/               # Utilitaires (DB, etc.)
│   ├── 📁 view/                # Interfaces utilisateur
│   └── Main.java               # Point d'entrée
├── 📁 bin/                     # Classes compilées
├── 📁 lib/                     # Bibliothèques externes
│   └── mysql-connector-j-9.4.0.jar
├── 📁 sql/                     # Scripts de base de données
│   └── database.sql
└── README.md                   # Ce fichier
```

## 👥 Types d'Utilisateurs

### 🔹 OUVRIER
- Consultation de ses propres paiements
- Mise à jour de son profil

### 🔹 RESPONSABLE_DEPARTEMENT
- Gestion des agents de son département
- Validation des paiements de son équipe
- Attribution de primes

### 🔹 DIRECTEUR
- Accès complet au système
- Gestion globale des agents et départements
- Validation finale des paiements

### 🔹 STAGIAIRE
- Accès limité en lecture seule
- Consultation de ses informations


## 🛠️ Technologies Utilisées

- **Langage** : Java 8+
- **Base de données** : MySQL 8.0
- **Driver JDBC** : MySQL Connector/J 9.4.0
- **Architecture** : MVC (Model-View-Controller)
- **Interface** : Console (CLI)

## 🚀 Prochaines Améliorations

- [ ] Interface graphique (JavaFX/Swing)
- [ ] API REST
- [ ] Système de notifications
- [ ] Export des rapports (PDF/Excel)
- [ ] Authentification 2FA
- [ ] Logs d'audit
- [ ] Configuration externalisée

---

*Développé avec ❤️ pour la gestion efficace des paiements d'agents*