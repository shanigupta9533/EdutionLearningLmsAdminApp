package com.edutionLearning.core.type

/**
 * Given a nullable list of nullable items (i.e. the worst type of data as far as null handling is concerned)
 * this returns a nullable list of null safe items.
 */
fun <T> Collection<T?>?.nullableListOfNullSafeItems(): List<T>? {

    /**
     * From docs - This is possible as something similar is what allows one to call toString()
     * in Kotlin without checking for null: the check happens inside the extension function.
     */
    if (this == null) {
        return null;
    }

    val arr = arrayListOf<T>()

    for (item in this) {
        item?.let {
            arr.add(item)
        }
    }

    return arr;
}

/**
 * Given a nullable list of nullable items (i.e. the worst type of data as far as null handling is concerned)
 * this returns a null safe list of null safe items.
 */
fun <T> Collection<T?>?.nullSafeListOfNullSafeItems(): List<T> {

    val arr = arrayListOf<T>()

    this?.let {
        for (item in it) {
            item?.let {
                arr.add(item)
            }
        }
    }

    return arr;
}

/**
 * Given a nullable list of nullable items (i.e. the worst type of data as far as null handling is concerned)
 * this returns a null safe list of nullable items.
 */
fun <T> Collection<T?>?.nullSafeListOfNullableItems(): List<T?> {

    val arr = arrayListOf<T?>()

    this?.let {
        arr.addAll(this)
    }

    return arr;
}