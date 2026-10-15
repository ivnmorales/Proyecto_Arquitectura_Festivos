const festivoService =
    require("../services/festivo.service");


// =========================
// LISTAR CONFIGURACIÓN
// =========================
async function listarConfiguracion(req, res) {

    try {

        const tipos =
            await festivoService
                .listarConfiguracion();


        return res.status(200).json(
            tipos
        );

    } catch (error) {

        return res.status(500).json({
            mensaje:
                "Error al consultar los festivos",
            error: error.message
        });
    }
}


// =========================
// LISTAR FESTIVOS POR AÑO
// =========================
async function listarPorAnio(req, res) {

    try {

        const anio =
            Number(req.params.anio);


        const festivos =
            await festivoService
                .listarFestivosPorAnio(
                    anio
                );


        return res.status(200).json(
            festivos
        );

    } catch (error) {

        return res.status(500).json({
            mensaje:
                "Error al calcular los festivos",
            error: error.message
        });
    }
}


// =========================
// VERIFICAR FECHA
// =========================
async function verificarFecha(req, res) {

    try {

        const anio =
            Number(req.params.anio);

        const mes =
            Number(req.params.mes);

        const dia =
            Number(req.params.dia);


        const resultado =
            await festivoService
                .verificarFecha(
                    anio,
                    mes,
                    dia
                );


        return res.status(200).json(
            resultado
        );

    } catch (error) {

        return res.status(500).json({
            mensaje:
                "Error al verificar la fecha",
            error: error.message
        });
    }
}


// =========================
// CREAR FESTIVO
// =========================
async function crearFestivo(req, res) {

    try {

        const tipoId =
            Number(req.params.tipoId);


        const festivo =
            await festivoService
                .crearFestivo(
                    tipoId,
                    req.body
                );


        return res.status(201).json({
            mensaje:
                "Festivo creado correctamente",
            festivo
        });

    } catch (error) {

        // =========================
        // 404 - RECURSO NO EXISTE
        // =========================
        if (
            error.message ===
            "El tipo de festivo no existe"
        ) {
            return res.status(404).json({
                mensaje: error.message
            });
        }


        // =========================
        // 400 - ERROR DEL CLIENTE
        // =========================
        if (
            error.message ===
                "Ya existe un festivo con ese nombre" ||

            error.message ===
                "El día y el mes no forman una fecha válida" ||

            error.message ===
                "Este tipo de festivo requiere diasPascua" ||

            error.message ===
                "diasPascua debe ser un número entero"
        ) {
            return res.status(400).json({
                mensaje: error.message
            });
        }


        // =========================
        // 500 - ERROR INTERNO / BD
        // =========================
        return res.status(500).json({
            mensaje:
                "Error interno al crear el festivo",
            error: error.message
        });
    }
}


// =========================
// OBTENER FESTIVO
// =========================
async function obtenerFestivo(req, res) {

    try {

        const tipoId =
            Number(req.params.tipoId);

        const nombre =
            req.params.nombre;


        const festivo =
            await festivoService
                .obtenerFestivo(
                    tipoId,
                    nombre
                );


        if (!festivo) {

            return res.status(404).json({
                mensaje:
                    "Festivo no encontrado"
            });
        }


        return res.status(200).json(
            festivo
        );

    } catch (error) {

        return res.status(500).json({
            mensaje:
                "Error al consultar el festivo",
            error: error.message
        });
    }
}


// =========================
// ACTUALIZAR FESTIVO
// =========================
async function actualizarFestivo(req, res) {

    try {

        const tipoId =
            Number(req.params.tipoId);

        const nombre =
            req.params.nombre;


        const festivo =
            await festivoService
                .actualizarFestivo(
                    tipoId,
                    nombre,
                    req.body
                );


        return res.status(200).json({
            mensaje:
                "Festivo actualizado correctamente",
            festivo
        });

    } catch (error) {

        // =========================
        // 404 - RECURSO NO EXISTE
        // =========================
        if (
            error.message ===
                "El festivo no existe" ||

            error.message ===
                "El tipo de festivo no existe"
        ) {
            return res.status(404).json({
                mensaje: error.message
            });
        }


        // =========================
        // 400 - ERROR DEL CLIENTE
        // =========================
        if (
            error.message ===
                "Ya existe un festivo con ese nombre" ||

            error.message ===
                "El día y el mes no forman una fecha válida" ||

            error.message ===
                "Este tipo de festivo requiere diasPascua" ||

            error.message ===
                "diasPascua debe ser un número entero"
        ) {
            return res.status(400).json({
                mensaje: error.message
            });
        }


        // =========================
        // 500 - ERROR INTERNO / BD
        // =========================
        return res.status(500).json({
            mensaje:
                "Error interno al actualizar el festivo",
            error: error.message
        });
    }
}


// =========================
// ELIMINAR FESTIVO
// =========================
async function eliminarFestivo(req, res) {

    try {

        const tipoId =
            Number(req.params.tipoId);

        const nombre =
            req.params.nombre;


        const eliminado =
            await festivoService
                .eliminarFestivo(
                    tipoId,
                    nombre
                );


        if (!eliminado) {

            return res.status(404).json({
                mensaje:
                    "Festivo no encontrado"
            });
        }


        return res.status(200).json({
            mensaje:
                "Festivo eliminado correctamente"
        });

    } catch (error) {

        return res.status(500).json({
            mensaje:
                "Error al eliminar el festivo",
            error: error.message
        });
    }
}


// =========================
// EXPORTACIONES
// =========================
module.exports = {
    listarConfiguracion,
    listarPorAnio,
    verificarFecha,
    crearFestivo,
    obtenerFestivo,
    actualizarFestivo,
    eliminarFestivo
};