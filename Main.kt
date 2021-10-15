package search

import java.io.File
import java.util.*

val lines = mutableListOf<String>()
val maps = mutableMapOf<String, MutableList<Int>>()
var sc = Scanner(System.`in`)
var inputFile = "text.txt"

fun main(args: Array<String>){
    if (args[0] == "--data") inputFile = args[1]

        readList()
    while (true) {
        when (menu()) {
            1 -> find()
            2 -> printList()
            0 -> break
        }
    }
}

fun menu(): Int {
    var item = 0
    while (true) {
        println()
        println(
            """
=== Menu ===
1. Find a person
2. Print all people
0. Exit
    """.trimIndent()
        )
        item = readLine()!!.toInt()
        if (item !in 0..2) {
            println()
            println("Incorrect option! Try again.\n")
        } else break
    }
    return item
}

fun find() {
    println()
    println("Select a matching strategy: ALL, ANY, NONE")
    val what = readLine()!!
    println()
    println("Enter a name or email to search all suitable people.")
    val word = readLine()!!.lowercase()
    when (what) {
        "ALL" -> findAll(word)
        "ANY" ->  findAny(word)
        "NONE" ->  findNone(word)
    }
}

fun printList() {
    println()
    println("=== List of people ===")
    for (l in lines) {
        println(l)
    }
}

fun readList() {
    val scanner = Scanner(File(inputFile))
    var k = 0
    while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine())
        for (word in lines.last().split(" ")) {
            val w = word.lowercase()
            maps.getOrPut(w) { mutableListOf() }.add(k)
        }
        k++
    }
    scanner.close()
}

fun findAny(query: String) {
    querySet(query).forEach() { println(lines[it]) }
}

fun findAll(query: String) {
    val q = query.split(" ")
    val set = querySet(query)
    val set1 = set.toMutableSet()
    set.forEach() { s ->
        q.forEach() { if (!maps[it]!!.contains(s)) set1.remove(s) }
    }
    if (set1.isEmpty()) {
        println("No matching people found.")
    } else {
        set1.forEach() { println(lines[it]) }
    }
}

fun findNone(query: String) {
    val set = querySet(query)
    lines.indices.filter { i -> i !in set }.forEach() { println(lines[it])}
}

fun querySet(query: String): Set<Int> {
    val find = mutableListOf<Int>()
    val q = query.split(" ")
    q.forEach {
        if (maps[it] != null) {
            find += maps[it]!!
        }
    }
    return find.toSet()
}