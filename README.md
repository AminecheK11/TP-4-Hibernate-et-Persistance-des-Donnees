# TP Hibernate — Persistance des données avec Hibernate et MySQL

**Réalisé par : Amine Chekkouri**

---

## Objectif

L'objectif de ce TP est de mettre en place une application Java utilisant **Hibernate ORM** et **MySQL** pour gérer la persistance des données.

Le but est de comprendre le fonctionnement d'un ORM et de mettre en place une architecture organisée en plusieurs couches.

Plusieurs notions ont été étudiées :

- Configuration de Hibernate avec Maven
- Interface générique `IDao<T>`
- Entités JPA `Salle` et `Machine`
- Relation `OneToMany` / `ManyToOne`
- Opérations CRUD
- Couche DAO et couche Service
- Requêtes HQL
- Gestion des transactions et du rollback
- Tests avec JUnit 5

---

## Technologies utilisées

- Java 17
- Hibernate ORM 5.6.15.Final
- JPA
- Maven
- MySQL
- MySQL Connector/J 8.0.33
- JUnit 5.9.2
- IntelliJ IDEA
- MySQL Workbench

---

## 1. Structure du projet

Le projet est organisé en plusieurs packages afin de séparer les responsabilités :

- `dao` : contient l'interface générique `IDao<T>` et les classes d'accès aux données.
- `entities` : contient les entités `Salle` et `Machine`.
- `services` : contient la couche service.
- `util` : contient la classe `HibernateUtil`.
- `resources` : contient le fichier `hibernate.cfg.xml`.
- `test` : contient la classe principale de test.
- `src/test/java` : contient les tests JUnit.

### Capture 1 : Structure finale du projet

<img width="480" height="767" alt="cap0_tp4" src="https://github.com/user-attachments/assets/704917f3-5be0-43d3-97a3-6ce4717b7ebf" />


---

## 2. Configuration de Hibernate

Hibernate est configuré à l'aide du fichier `hibernate.cfg.xml`.

Ce fichier permet de définir la connexion à la base de données MySQL, le driver JDBC, le dialecte MySQL ainsi que les entités gérées par Hibernate.

La classe `HibernateUtil` permet de créer la `SessionFactory` utilisée pour ouvrir les sessions Hibernate et communiquer avec la base de données.

---

## 3. Entités Salle et Machine

Deux entités principales ont été créées :

- `Salle` : représente une salle avec un code et un libellé.
- `Machine` : représente une machine avec une référence et une marque.

Une relation **One-to-Many** est définie entre `Salle` et `Machine`.

Une salle peut contenir plusieurs machines, tandis qu'une machine appartient à une seule salle.

Cette relation est représentée dans la base de données grâce à la clé étrangère `salle_id`.

---

## 4. Insertion et récupération des données

La classe `MainTest` permet de vérifier le fonctionnement de la persistance.

Une salle est enregistrée avec les informations suivantes :

- Code : `S1`
- Libellé : `Salle Informatique`

### Capture 2 : Insertion et récupération de la salle

<img width="421" height="254" alt="cap1_tp4" src="https://github.com/user-attachments/assets/6003aa94-44d2-4a0d-aa02-c55395536363" />


La console confirme que les données ont été insérées avec succès et que la salle enregistrée est :

**Salle 1 : S1 - Salle Informatique**

---

## 5. Association entre Machine et Salle

Une machine est ensuite enregistrée avec les informations suivantes :

- Référence : `M-101`
- Marque : `Dell`
- Salle : `S1`

La machine est donc associée à la salle précédemment créée.

### Capture 3 : Récupération de la machine et de sa salle

<img width="674" height="553" alt="cap1 1_tp4" src="https://github.com/user-attachments/assets/421b6b43-f351-4630-ac75-dbd6dba0d5ea" />


La console affiche :

**Machine Dell 1 : M-101, salle=S1**

Elle confirme également que la salle contient une machine :

**Machines de S1 : 1**

L'exécution se termine correctement avec :

**Process finished with exit code 0**

---

## 6. Vérification de la table Salle dans MySQL

Les données enregistrées avec Hibernate ont été vérifiées directement dans **MySQL Workbench**.

La requête `SELECT * FROM salles;` permet de consulter les salles enregistrées dans la base de données.

### Capture 4 : Contenu de la table salles

<img width="1162" height="766" alt="cap2_tp4" src="https://github.com/user-attachments/assets/182d5c26-9524-4904-a392-5177de2c9746" />


La table contient :

- `id = 1`
- `code = S1`
- `libelle = Salle Informatique`

Cela confirme que l'objet Java `Salle` a bien été persisté dans MySQL.

---

## 7. Vérification de la table Machine dans MySQL

La requête `SELECT * FROM machines;` permet de consulter les machines enregistrées.

### Capture 5 : Contenu de la table machines

<img width="1122" height="837" alt="cap2 1_tp4" src="https://github.com/user-attachments/assets/8f5eef26-48d1-498c-a3f6-20f8a4de733f" />


La table contient :

- `id = 1`
- `marque = Dell`
- `ref = M-101`
- `salle_id = 1`

La valeur `salle_id = 1` confirme que la machine `M-101` est correctement associée à la salle `S1`.

---

## 8. Tests unitaires avec JUnit

Des tests JUnit ont été réalisés afin de vérifier automatiquement les différentes fonctionnalités de l'application.

Les tests permettent notamment de vérifier :

- la création des salles et des machines ;
- la récupération des données ;
- la modification ;
- la suppression ;
- les associations entre les entités ;
- les requêtes HQL ;
- la gestion des transactions et du rollback.

### Capture 6 : Exécution de l'ensemble des tests

<img width="814" height="654" alt="cap3_tp4" src="https://github.com/user-attachments/assets/16f30edc-8c2c-419d-9bff-0b28a002387a" />


Le résultat obtenu est :

**17 tests passed — 17 tests total**

Cela confirme que l'ensemble des tests exécutés est validé.

---

## 9. Recherche des machines avec HQL

Hibernate propose le langage **HQL (Hibernate Query Language)** permettant d'effectuer des requêtes en utilisant directement les entités Java et leurs propriétés.

Une méthode de recherche par marque a été mise en place pour rechercher les machines correspondant à une marque donnée.

Le test `testRechercheMarque()` permet de vérifier cette fonctionnalité.

### Capture 7 : Test de recherche par marque

<img width="509" height="182" alt="cap4_tp4" src="https://github.com/user-attachments/assets/4d211548-1e7c-4ea5-b896-dcadc9f4da24" />


Le résultat **1 test passed** confirme que la recherche des machines par marque fonctionne correctement.

---

## 10. Gestion des transactions et rollback

Les opérations de persistance sont réalisées à l'intérieur de transactions Hibernate.

Lorsqu'une opération est réalisée correctement, la transaction est validée avec un `commit`.

En cas d'erreur, un `rollback` permet d'annuler les modifications afin de conserver la cohérence de la base de données.

Le test `testRollbackUpdateInvalide()` permet de vérifier ce comportement.

### Capture 8 : Test du rollback

<img width="723" height="455" alt="cap5_tp4" src="https://github.com/user-attachments/assets/4e45f4b0-ef09-4334-bf48-250202424018" />


Le résultat **1 test passed** confirme que la gestion transactionnelle fonctionne correctement.

---

## 11. Architecture DAO et Service

L'interface générique `IDao<T>` permet de standardiser les principales opérations CRUD :

- `create()`
- `delete()`
- `update()`
- `getById()`
- `getAll()`

Les classes `SalleDao` et `MachineDao` permettent d'effectuer les opérations de persistance avec Hibernate.

Les classes `SalleService` et `MachineService` constituent la couche Service et utilisent les DAO pour accéder aux données.

Cette organisation permet de séparer la logique de l'application de la logique d'accès à la base de données.

---

## 12. Gestion des transactions

Les transactions Hibernate permettent de garantir l'intégrité des données lors des opérations d'écriture.

Les principales opérations utilisées sont :

- `beginTransaction()` pour démarrer une transaction.
- `commit()` pour valider les modifications.
- `rollback()` pour annuler les modifications en cas d'erreur.

Cette gestion permet d'éviter les modifications partielles ou incohérentes dans la base de données.

---

## Conclusion

Ce TP a été l'occasion de mettre en pratique Hibernate ORM pour assurer la persistance des données d'une application Java avec MySQL.

La réalisation du projet repose sur une architecture structurée autour de l'interface générique IDao<T>, des entités Salle et Machine, des DAO et des services. Les relations JPA ont également été utilisées pour gérer l'association entre les salles et les machines.

Les requêtes HQL ont été utilisées pour rechercher les données, tandis que JUnit 5 a servi à valider les opérations CRUD, les associations ainsi que la gestion des transactions.

Ce TP met ainsi en évidence l'intérêt d'Hibernate pour construire une application structurée, maintenable et testable, tout en facilitant la communication entre les objets Java et la base de données MySQL.
