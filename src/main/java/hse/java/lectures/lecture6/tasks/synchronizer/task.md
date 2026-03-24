# Синхронизация вечных потоков по `id`

`CI-тег коммита:` `synchronizer:`

## Условие

Реализовать синхронизатор для `N` потоков-писателей (`N >= 2`).

Каждый писатель (`StreamWriter`) описывает один тик печати: при вызове `run()` пишет свой символ в общий поток (`PrintStream`) и вызывает `onTick`.

Бесконечность процесса должна обеспечиваться на уровне потоков в `Synchronizer`: каждый worker-поток циклически вызывает свой `StreamWriter.run()` в бесконечном цикле.

Задача синхронизатора:

1. Запустить все потоки.
1. Упорядочить вывод строго по `id` писателей (по возрастанию).
1. Взять только первые `ticksPerWriter` тиков от каждого писателя.
1. После получения `N * ticksPerWriter` символов завершить `execute()`.

Если `id=1 -> A`, `id=2 -> B`, `id=3 -> C` и `ticksPerWriter=10`, ожидаемый результат:

`ABCABCABCABCABCABCABCABCABCABC`

## Контракты

1. `StreamWriter` (один тик):
`StreamWriter(int id, String message, PrintStream output, Runnable onTick)`
`id` — уникальный идентификатор писателя.
`message` — символ/строка для печати за один тик.
`onTick` — callback, который вызывается на каждом тике (для тестов и метрик).
1. `StreamWriter.run()` должен быть бесконечным циклом:
писатель ждёт разрешения в monitor, печатает тик, уведомляет monitor о завершении тика.
1. `Synchronizer` (управляет бесконечными worker-потоками):
`Synchronizer(List<StreamWriter> tasks)`
`Synchronizer(List<StreamWriter> tasks, int ticksPerWriter)`
`void execute()`
1. Общий monitor/state пробрасывается в каждый `StreamWriter` перед стартом потоков
(через метод, конструктор или другой явный механизм).
