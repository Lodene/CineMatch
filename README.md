# Guide d'installation

### Disclaimer

Le projet a été développé avec python 3.10, podman 5.4.1 pour les containers. Il n'a pas été entièrement testé avec Docker

### Models

Pour utiliser le modèle entrainer, il faut télécharger ces 3 fichiers & les insérer dans le dossier `ai-movie_clustering\core\models` (qu'il faut créer)
[LIEN](https://drive.google.com/drive/folders/1MAPzjNwrn2PnFkw-iP5Wg7mDXjE2Sc8m?usp=sharing)

### IMPORTANT

- Il faut générer les inserts pour la db liquibase (cinematch-backend).
- Il faut soit compiler le common-module à la main, soit lancer le script à la racine du projet `install.ps1` (si vous avez podman, sinon lancer le & exécuter ensuite votre docker-compose).
- La database étant très grande, le premier lancement du `spring-back service` risque d'être très long (environ 15 à 20 min).
- Nécessite environ 12Go de mémoire RAM pour faire convenablement tourné l'AI avec les containers.

#### Dataset

Il y a 5 datasets.
- [TMDB_all_movies](https://www.kaggle.com/datasets/alanvourch/tmdb-movies-daily-updates) 
- [subset](https://drive.google.com/file/d/1Njzi1B8F-saefw0dihhMi-zdCNww4UPt/view?usp=sharing)
- [films_enriched_full](https://drive.google.com/file/d/1ARL6h_zFI9pk0es2ddnDE8tij_tr71hM/view?usp=sharing)
- [films_enriched_omdb](https://drive.google.com/file/d/1bGFvr3MQBDwEDhj3O2HbYApPH3RiFRIE/view?usp=sharing)
- [films_enriched_wiki](https://drive.google.com/file/d/1D99_ETHXZ5E-8snlvsSRY8aScrx9Mn7v/view?usp=sharing)

Placer le fichier subset.csv dans le dossier `dataset-to-liquibase` 

Placer les 5 datasets dans le dossier `python-api\core\data` (il faut les créer)

Placer les aussi dans le dossier `ai-movie_clustering\core\data` (il faut les créer)


#### Film

Pour ce faire, lancer le script `install_full_db.ps1` depuis le dossier `dataset-to-liquibase\`


Il va générer des changesets pour insertion dans la db. (table movie & ses adjacents) & les inclures dans le projet `cinematch-backend`. Le masterchangelog de liquibase est configuré pour les exécuter (il plantera s'il ne les trouve pas).


#### Film similaire

Pour intégrer les films similaires il faut les générer depuis l'outils AI.
NE PAS SE SERVIR de python-api, utiliser directement le modèle en projet python: 

Ouvrir le dossier ai-movie_clustering

Se référencer au readme du projet pour le lancement du projet AI

** Il faut bien avoir installer le projet pour utiliser les aliases avec **: 
`uv run pip install -e .`

Commande pour générer le liquibase:

`uv run --env-file=.env liquibase`

S'il ne trouve pas l'alias, lancer le script directement avec uv en précisant le chemin (eg. .\core\scripts\reco_to_liquibase)

Dans le dossier output, exécuter le script `create_changeset.py`

Cela génèrera un fichier changeset.xml

Copier l'intégralité tous les fichiers de liquibase_xml vers le dossier `cinematch-backend-service\cinematch-backend\src\main\resources\db\changelog\similar-movie-chunks`

Renommer le fichier `changeset.xml` en `similar-movie-insert.xml` (le nom doit correspondre à celui du master changelog liquibase).

#### Express socket

En raison d'un mauvais dockerfile, il faut d'abord `npm install` le projet avant de lancer le `docker-compose`

#### Authors

- Marin Thomas
- Al-Homsi Raghad
- Rtayli Oumaima
- Chatelet Mathias
- Adjamidis Antoine





