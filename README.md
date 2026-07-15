<p align="center">
  <img src="src/main/resources/META-INF/pluginIcon.svg" width="96" alt="Jumper icon">
</p>

<h1 align="center">Jumper</h1>

<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/17449-jumper"><img src="https://img.shields.io/jetbrains/plugin/v/17449-jumper?label=marketplace" alt="Marketplace version"></a>
  <a href="https://plugins.jetbrains.com/plugin/17449-jumper"><img src="https://img.shields.io/jetbrains/plugin/d/17449-jumper" alt="Downloads"></a>
  <a href="https://plugins.jetbrains.com/plugin/17449-jumper/reviews"><img src="https://img.shields.io/jetbrains/plugin/r/rating/17449-jumper" alt="Rating"></a>
</p>

<p align="center">A JetBrains IDE plugin for moving the caret several lines per keypress. Bind "jump 5 lines" to a key and move through files, project trees, and lists without reaching for the mouse or hammering the arrow keys.</p>

<!-- Plugin description -->
Jumper moves your caret several lines at a time with a single keybinding. Define any jump size you like, bind keys to it, and hold them down to move through code like you would with the arrow keys, just several lines per step.

Jumps work almost everywhere in the IDE: the editor, project view, Gradle view, database view, and any other tree or list component.

**Features**

- Any jump size, and as many different sizes as you want
- Four actions per jump: up, down, and both with selection
- Works in editors, trees, and lists across the whole IDE
- Settings panel with integrated keybinding setup and a live overview of your bindings

**Quick start**

1. Go to <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Jumper Settings</kbd>
2. Enter a jump amount (for example `5`) and click **Add Jump**
3. Bind keys for the four actions (Up, Down, Up with Selection, Down with Selection)
4. Use them anywhere in the IDE
<!-- Plugin description end -->

## Installation

**From the IDE (recommended):** <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > search for "Jumper" > <kbd>Install</kbd>

**From the web:** the [Jumper page on JetBrains Marketplace](https://plugins.jetbrains.com/plugin/17449-jumper).

## Compatibility

Works in all JetBrains IDEs (IntelliJ IDEA, PyCharm, WebStorm, Rider, CLion, and the rest) on platform 2025.1 or newer. The plugin only depends on the core platform, so no IDE-specific modules are required.

## Building from source

```bash
./gradlew buildPlugin      # produces the plugin zip under build/distributions
./gradlew runIde           # launches a sandbox IDE with the plugin installed
```

See [CHANGELOG.md](CHANGELOG.md) for release history.

---

Icon by Benjamin Hoffmeyer.
