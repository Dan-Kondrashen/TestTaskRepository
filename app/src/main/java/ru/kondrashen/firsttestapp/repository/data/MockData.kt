package ru.kondrashen.firsttestapp.repository.data

class CheckDTO(
    val uuid: Int,
    val operationDataTime: Long,
    val checkOperationType: CheckOperation,
    val vehicleName: String,
    val operationWeight: Int,
    val hasPrinted: Boolean,
)

enum class CheckOperation {
    LOAD,
    UNLOAD;
}


val mockList = listOf(
    CheckDTO(0, 1685614507000L, CheckOperation.LOAD, "Комбайн: A456EP", 4450, false),
    CheckDTO(1, 1685614987000L, CheckOperation.LOAD, "Комбайн: B112MT", 3500, false),
    CheckDTO(2, 1685615107L, CheckOperation.LOAD, "Комбайн: C356PK", 4700, false),
    CheckDTO(3, 1685614507000L, CheckOperation.UNLOAD, "Зерновоз: E566KP", 12500, false),
    CheckDTO(4, 1685616307L, CheckOperation.LOAD, "Комбайн: A456EP", 3700, false),
    CheckDTO(5, 1685616613L, CheckOperation.LOAD, "Комбайн: B112MT", 4100, false),
    CheckDTO(6, 1685617025L, CheckOperation.LOAD, "Комбайн: C356PK", 2500, false),
    CheckDTO(7, 1685617249L, CheckOperation.UNLOAD, "Зерновоз: E566KP", 10300, false),
)