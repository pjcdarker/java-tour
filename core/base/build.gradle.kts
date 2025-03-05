plugins {
    id("java-conventions")
}

tasks.named<Test>("test") {
    jvmArgs(
        "--add-opens", "java.base/sun.reflect.annotation=ALL-UNNAMED",
    )
}