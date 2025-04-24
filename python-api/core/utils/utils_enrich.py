from typing import Optional, Tuple

import requests
from config import ENRICHMENT_DONE, ENRICHMENT_FAIL


def get_wikipedia_url_from_imdb(imdb_id: str) -> Optional[str]:
    """
    Get a Wikipedia page URL based on an IMDB ID, using Sparql.

    Args:
        imdb_id (str): IMDB ID.

    Returns:
        Optional[str]: Wikipedia URL if found, else None.
    """
    query = f"""
    SELECT ?wikiPage WHERE {{
        ?film wdt:P345 "{str(imdb_id)}".
        ?wikiPage schema:about ?film.
        ?wikiPage schema:isPartOf <https://en.wikipedia.org/>.
    }}
    """
    url = "https://query.wikidata.org/sparql"
    headers = {
        "Accept": "application/sparql-results+json",
    }
    r = requests.get(url, params={"query": query}, headers=headers)
    data = r.json()

    try:
        return data["results"]["bindings"][0]["wikiPage"]["value"]
    except IndexError:
        return None


def get_wiki_summary_from_url(url: Optional[str]) -> Tuple[Optional[str], str]:
    """
    Get movie summary from a Wikipedia URL, using their REST API.

    Args:
        url (Optional[str]): Wikipedia URL, if any.

    Returns:
        Tuple[Optional[str], str]: Tuple (movie summary if any, enrichment status).
    """
    if not url:
        return None, ENRICHMENT_FAIL
    try:
        title = url.split("/")[-1]
        api_url = f"https://en.wikipedia.org/api/rest_v1/page/summary/{title}"
        r = requests.get(api_url)
        if r.status_code == 200:
            data = r.json()
            return data.get("extract", None), ENRICHMENT_DONE
    except Exception as e:
        print(f"[ERROR] Error SQL query {url} -W {e}")
        return None, ENRICHMENT_FAIL
    return None, ENRICHMENT_FAIL
