const Tipo = require("../models/tipo.model");


// =========================
// NORMALIZAR NOMBRE
// =========================

function normalizarNombre(nombre) {
    return String(nombre)
        .trim()
        .toLowerCase();
}


// =========================
// OBTENER TODOS LOS TIPOS
// =========================

async function obtenerTipos() {
    return await Tipo
        .find()
        .sort({ id: 1 });
}


// =========================
// OBTENER TIPO POR ID
// =========================
async function obtenerTipoPorId(tipoId) {
    return await Tipo.findOne({
        id: Number(tipoId)
    });
}


// =========================
// OBTENER UN FESTIVO
// =========================
async function obtenerFestivo(
    tipoId,
    nombre
) {
    const tipo =
        await obtenerTipoPorId(tipoId);

    if (!tipo) {
        return null;
    }


    const festivo =
        tipo.festivos.find(
            (item) =>
                normalizarNombre(item.nombre) ===
                normalizarNombre(nombre)
        );


    return festivo || null;
}


// =========================
// CREAR FESTIVO
// =========================
async function crearFestivo(
    tipoId,
    festivo
) {
    const tipo =
        await obtenerTipoPorId(tipoId);

    if (!tipo) {
        return null;
    }


    tipo.festivos.push(festivo);

    await tipo.save();


  
    return tipo.festivos[
        tipo.festivos.length - 1
    ];
}


// =========================
// ACTUALIZAR FESTIVO
// =========================
async function actualizarFestivo(
    tipoId,
    nombreActual,
    nuevosDatos
) {
    const tipo =
        await obtenerTipoPorId(tipoId);

    if (!tipo) {
        return null;
    }


    const festivo =
        tipo.festivos.find(
            (item) =>
                normalizarNombre(item.nombre) ===
                normalizarNombre(nombreActual)
        );


    if (!festivo) {
        return null;
    }


    
    if (nuevosDatos.dia !== undefined) {
        festivo.dia = nuevosDatos.dia;
    }

    if (nuevosDatos.mes !== undefined) {
        festivo.mes = nuevosDatos.mes;
    }

    if (nuevosDatos.nombre !== undefined) {
        festivo.nombre =
            nuevosDatos.nombre;
    }

    if (
        nuevosDatos.diasPascua !==
        undefined
    ) {
        festivo.diasPascua =
            nuevosDatos.diasPascua;
    }


    await tipo.save();

    return festivo;
}


// =========================
// ELIMINAR FESTIVO
// =========================
async function eliminarFestivo(
    tipoId,
    nombre
) {
    const tipo =
        await obtenerTipoPorId(tipoId);

    if (!tipo) {
        return false;
    }


    const cantidadInicial =
        tipo.festivos.length;


    tipo.festivos =
        tipo.festivos.filter(
            (item) =>
                normalizarNombre(item.nombre) !==
                normalizarNombre(nombre)
        );


  
    if (
        tipo.festivos.length ===
        cantidadInicial
    ) {
        return false;
    }


    await tipo.save();

    return true;
}


// =========================
// EXPORTACIONES
// =========================
module.exports = {
    obtenerTipos,
    obtenerTipoPorId,
    obtenerFestivo,
    crearFestivo,
    actualizarFestivo,
    eliminarFestivo
};