# Случайное множество (RandomSet)

Tag: randomset

## Условие

Реализовать класс `RandomSet`, который хранит множество целых чисел и поддерживает операции вставки, удаления и получения случайного элемента.

## Требования к асимптотике

- `insert(x)` — **O(log n) or O(1)**
- `remove(x)` — **O(log n) or O(1)**
- `contains(x)` — **(log n) or O(1)**
- `getRandom()` — **O(1)**

Не допускается использование стандартных коллекций Java.

## Публичный API

1. `RandomSet()` — создаёт пустое множество.
1. `boolean insert(T value)` — добавляет `value` в множество.
   - Возвращает `true`, если элемент добавлен.
   - Возвращает `false`, если элемент уже был в множестве.
1. `boolean remove(T value)` — удаляет `value` из множества.
   - Возвращает `true`, если элемент был удалён.
   - Возвращает `false`, если элемента не было.
1. `boolean contains(T value)` — проверяет наличие `value` в множестве.
   - Возвращает `true`, если элемент есть.
   - Возвращает `false`, если элемента нет.
1. `T getRandom()` — возвращает случайный элемент из множества.
   - Если множество пустое, выбрасывает `EmptySetException`.

## Исключения

- `EmptySetException` — попытка получить случайный элемент из пустого множества.

## Пример

```java
RandomSet<Integer> set = new RandomSet<>();
set.insert(10); // true
set.insert(20); // true
set.insert(10); // false

int x = set.getRandom(); // 10 или 20

set.remove(10); // true
set.remove(10); // false

set.contains(20); // true
set.contains(30); // false
```
