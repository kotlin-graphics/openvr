package main.helloVr_OpenGL

import glm_.bool
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.stream.Collectors

open class Program {

    @JvmField
    var name = GL20.glCreateProgram()
    val uniforms = HashMap<String, Int>()

    constructor()

    // for Learn OpenGL

    /** (root, vertex, fragment) or (vertex, fragment)  */
    constructor(context: Class<*>, vararg strings: String) {

        val root =
                if (strings[0].isShaderPath())
                    ""
                else {
                    var r = strings[0]
                    if (r[0] != '/')
                        r = "/$r"
                    if (!r.endsWith('/'))
                        r = "$r/"
                    r
                }

        val (shaders, uniforms) = strings.drop(if (root.isEmpty()) 0 else 1).partition { it.isShaderPath() }

        val shaderNames = shaders.map { createShaderFromPath(context, root + it) }.onEach { GL20.glAttachShader(name, it) }

        GL20.glLinkProgram(name)

        if (GL20.glGetProgrami(name, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            System.err.println("Linker failure: ${GL20.glGetProgramInfoLog(name)}")

        shaderNames.forEach {
            GL20.glDetachShader(name, it)
            GL20.glDeleteShader(it)
        }

        uniforms.forEach {
            val i = GL20.glGetUniformLocation(name, it)
            if (i != -1)
                this.uniforms[it] = i
            else
                println("unable to find '$it' uniform location!")
        }
    }

    constructor(vertSrc: String, fragSrc: String) {

//        var root = ""
//
//        if (!strings[0].isShaderPath() && !strings[0].contains("#version")) { // TODO passing directly srcs
//            root = strings[0]
//            if (!root.endsWith('/'))
//                root = "$root/"
//        }

        val vert = createShader(vertSrc, GL_VERTEX_SHADER)
        val frag = createShader(fragSrc, GL_FRAGMENT_SHADER)

        attach(vert)
        attach(frag)

        link()

        if (!linkStatus)
            System.err.println("Linker failure: $infoLog")

        detach(vert)
        detach(frag)

        delete(vert)
        delete(frag)
    }

    operator fun get(s: String): Int = uniforms[s]!!

    private fun String.isShaderPath() = when (substringAfterLast('.')) {
        "vert", "tesc", "tese", "geom", "frag", "comp" -> true
        else -> false
    }


    fun createShaderFromPath(context: Class<*>, path: String): Int {

        val shader = GL20.glCreateShader(path.type)

        val url = context::class.java.getResource(path)
        val lines = File(url.toURI()).readLines()

        var source = ""
        lines.forEach {
            if (it.startsWith("#include "))
                source += parseInclude(context, path.substringBeforeLast('/'), it.substring("#include ".length).trim())
            else
                source += it
            source += '\n'
        }

        GL20.glShaderSource(shader, source)

        GL20.glCompileShader(shader)

        val status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS)
        if (status == GL11.GL_FALSE) {

            val strInfoLog = GL20.glGetShaderInfoLog(shader)

            System.err.println("Compiler failure in ${path.substringAfterLast('/')} shader: $strInfoLog")
        }

        return shader
    }

    fun parseInclude(context: Class<*>, root: String, shader: String): String {
        if (shader.startsWith('"') && shader.endsWith('"'))
            shader.substring(1, shader.length - 1)
        val url = context::class.java.getResource("$root/$shader")
        return File(url.toURI()).readText() + "\n"
    }

    fun createProgram(shaderList: List<Int>): Int {

        val program = GL20.glCreateProgram()

        shaderList.forEach { GL20.glAttachShader(program, it) }

        GL20.glLinkProgram(program)

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            System.err.println("Linker failure: ${GL20.glGetProgramInfoLog(program)}")

        shaderList.forEach {
            GL20.glDetachShader(program, it)
            GL20.glDeleteShader(it)
        }

        return program
    }

    fun attach(shader: Int) = GL20.glAttachShader(name, shader)
    operator fun plusAssign(shader: Int) = GL20.glAttachShader(name, shader)
    fun detach(shader: Int) = GL20.glDetachShader(name, shader)
    operator fun minusAssign(shader: Int) = GL20.glDetachShader(name, shader)

    fun delete(shader: Int) = GL20.glDeleteShader(shader)

    fun link() = GL20.glLinkProgram(name)

    val linkStatus get() = GL20.glGetProgrami(name, GL20.GL_LINK_STATUS).bool

    val infoLog get() = GL20.glGetProgramInfoLog(name)

    companion object {

        fun fromSources(vertSrc: String, fragSrc: String, geomSrc: String? = null): Program {

            val program = Program()

            val shaders = arrayListOf(createShader(vertSrc, GL20.GL_VERTEX_SHADER), createShader(fragSrc, GL20.GL_FRAGMENT_SHADER))
            geomSrc?.let { shaders += createShader(it, GL32.GL_GEOMETRY_SHADER) }

            shaders.forEach { GL20.glAttachShader(program.name, it) }

            program.link()

            if (GL20.glGetProgrami(program.name, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
                System.err.println("Linker failure: ${GL20.glGetProgramInfoLog(program.name)}")

            shaders.forEach {
                GL20.glDetachShader(program.name, it)
                GL20.glDeleteShader(it)
            }

            return program
        }

        @Throws(Error::class)
        fun createShader(source: String, type: Int): Int {

            val shader = GL20.glCreateShader(type)

            GL20.glShaderSource(shader, source)

            GL20.glCompileShader(shader)

            if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
                throw Error(GL20.glGetShaderInfoLog(shader))

            return shader
        }

        fun fromSources(vertSrc: Array<String>, fragSrc: Array<String>, geomSrc: Array<String>? = null): Program {

            val program = Program()

            val shaders = arrayListOf(createShader(vertSrc, GL20.GL_VERTEX_SHADER), createShader(fragSrc, GL20.GL_FRAGMENT_SHADER))
            geomSrc?.let { shaders += createShader(it, GL32.GL_GEOMETRY_SHADER) }

            shaders.forEach { GL20.glAttachShader(program.name, it) }

            program.link()

            if (GL20.glGetProgrami(program.name, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
                System.err.println("Linker failure: ${GL20.glGetProgramInfoLog(program.name)}")

            shaders.forEach {
                GL20.glDetachShader(program.name, it)
                GL20.glDeleteShader(it)
            }

            return program
        }

        @Throws(Error::class)
        fun createShader(source: Array<String>, type: Int): Int {

            val shader = GL20.glCreateShader(type)

            GL20.glShaderSource(shader, *source)

            GL20.glCompileShader(shader)

            if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
                throw Error(GL20.glGetShaderInfoLog(shader))

            return shader
        }

        fun createShaderFromPath(path: String): Int {

            val lines = ClassLoader.getSystemResourceAsStream(path).use {
                BufferedReader(InputStreamReader(it)).lines().collect(Collectors.toList())
            }

            var source = ""
            lines.forEach {
                source +=
                        if (it.startsWith("#include "))
                            parseInclude(path.substringBeforeLast('/'), it.substring("#include ".length).trim())
                        else it
                source += '\n'
            }

            try {
                return createShader(source, path.type)
            } catch (err: Exception) {
                throw Error("Compiler failure in ${path.substringAfterLast('/')} shader: ${err.message}")
            }
        }

        fun parseInclude(root: String, shader: String): String {
            if (shader.startsWith('"') && shader.endsWith('"'))
                shader.substring(1, shader.length - 1)
            val url = ClassLoader.getSystemResource("$root/$shader")
            return File(url.toURI()).readText() + "\n"
        }

        private val String.type
            get() = when (substringAfterLast('.')) {
                "vert" -> GL20.GL_VERTEX_SHADER
                "tesc" -> GL40.GL_TESS_CONTROL_SHADER
                "tese" -> GL40.GL_TESS_EVALUATION_SHADER
                "geom" -> GL32.GL_GEOMETRY_SHADER
                "frag" -> GL20.GL_FRAGMENT_SHADER
                "comp" -> GL43.GL_COMPUTE_SHADER
                else -> throw Error("invalid shader extension")
            }
    }
}