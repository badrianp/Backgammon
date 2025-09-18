# Backgammon (Java, CLI)

A simple Backgammon game implemented in Java as a console application.  
Includes a prototype server module (no client app provided).

---

## Build & Run (local game)

From the project root:

```bash
# Compile all sources
mkdir -p out
javac -d out $(find src -name "*.java")

# Run the local CLI game
java -cp out gameplay.Main

# Start the server (prototype, waiting for players)
java -cp out server.SimpleServer