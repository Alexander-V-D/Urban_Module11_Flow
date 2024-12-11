import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

suspend fun main() = coroutineScope {

    val persons = async {
        val persons = mutableListOf<Person>()
        getPersonFlow().collect { person -> persons.add(person) }
        persons
    }.await()

    val phones = async {
        val phones = mutableListOf<String>()
        getPhoneFlow(persons.size).collect {phone -> phones.add(phone)}
        phones
    }.await()

    val personsInfo = mutableListOf<String>()
    persons.forEachIndexed { index, person ->
        personsInfo.add("$person ${phones[index]}")
    }

    println(personsInfo)
}

fun getPersonFlow() = flow {
    val persons = listOf(
        Person("Алексей", "Менеджер"),
        Person("Сергей", "Разработчик"),
        Person("Геннадий", "Старший разработчик"),
        Person("Василий", "Стажер")
    )
    for (person in persons) {
        emit(person)
    }
}

fun getPhoneFlow(length: Int) = flow {
    repeat(length) {
        var number = "+7917"
        repeat(7) {
            number += (0..9).random().toString()
        }
        emit(number)
    }
}

