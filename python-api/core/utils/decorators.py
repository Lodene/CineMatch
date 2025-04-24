from time import time


def to_time(name: str):
    """
    Time a function.

    Args:
        name (str): Tag (for logging purposes).
    """

    def decorator(func):
        def wrapper(*args, **kwargs):
            print(f"[TIMER] Begin: '{name}'")
            start = time()
            result = func(*args, **kwargs)
            elapsed = round(time() - start, 2)
            print(f"[TIMER] End: '{name}' - {elapsed} sec\n")
            return result

        return wrapper

    return decorator
