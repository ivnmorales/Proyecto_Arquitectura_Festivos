const festivoRepository =
    require("../repositories/festivo.repository");

const calculoFechasService =
    require("./calculo-fechas.service");


// =========================
// LISTAR CONFIGURACIÓN
// =========================

async function listarConfiguracion() {
    return await festivoRepository.obtenerTipos();
}


// =========================
// LISTAR FESTIVOS POR AÑO
// =========================

async function listarFestivosPorAnio(anio) {

    const anioNumero = Number(anio);

  
    const tipos =
        await festivoRepository.obtenerTipos();


    return calculoFechasService
        .calcularFestivosAnio(
            tipos,
            anioNumero
        );
}


// =========================
// VERIFICAR FECHA
// =========================

async function verificarFecha(
    anio,
    mes,
    dia
) {
    const anioNumero = Number(anio);
    const mesNumero = Number(mes);
    const diaNumero = Number(dia);

    const festivos =
        await listarFestivosPorAnio(
            anioNumero
        );


    const fechaBuscada =
        `${anioNumero}-${String(mesNumero).padStart(2, "0")}-${String(diaNumero).padStart(2, "0")}`;


   
    const festivosEncontrados =
        festivos.filter(
            (festivo) =>
                festivo.fecha ===
                fechaBuscada
        );


    if (festivosEncontrados.length > 0) {
        return {
            fecha: fechaBuscada,
            esFestivo: true,
            festivos:
                festivosEncontrados.map(
                    (festivo) =>
                        festivo.nombre
                )
        };
    }


    return {
        fecha: fechaBuscada,
        esFestivo: false,
        festivos: []
    };
}

function validarFestivoSegunTipo(tipo, datosFestivo) {

    // Tipo 1 y 2:
    // requieren una fecha base con día y mes.
    if (tipo.id === 1 || tipo.id === 2) {

        if (
            datosFestivo.dia < 1 ||
            datosFestivo.mes < 1
        ) {
            throw new Error(
                "Este tipo de festivo requiere día y mes"
            );
        }
    }


    // Tipo 3 y 4:
    // se calculan con respecto a Pascua.
    if (tipo.id === 3 || tipo.id === 4) {

        if (
            datosFestivo.diasPascua ===
            undefined
        ) {
            throw new Error(
                "Este tipo de festivo requiere diasPascua"
            );
        }
    }
}


// =========================
// CREAR FESTIVO
// =========================
async function crearFestivo(
    tipoId,
    datosFestivo
) {
    const tipo =
        await festivoRepository
            .obtenerTipoPorId(tipoId);


    if (!tipo) {
        throw new Error(
            "El tipo de festivo no existe"
        );
    }

    validarFestivoSegunTipo(
    tipo,
    datosFestivo
    );


    const existente =
        await festivoRepository
            .obtenerFestivo(
                tipoId,
                datosFestivo.nombre
            );


    if (existente) {
        throw new Error(
            "Ya existe un festivo con ese nombre"
        );
    }


    return await festivoRepository
        .crearFestivo(
            tipoId,
            datosFestivo
        );
}


// =========================
// OBTENER FESTIVO
// =========================
async function obtenerFestivo(
    tipoId,
    nombre
) {
    return await festivoRepository
        .obtenerFestivo(
            tipoId,
            nombre
        );
}


// =========================
// ACTUALIZAR FESTIVO
// =========================
async function actualizarFestivo(
    tipoId,
    nombre,
    datosFestivo
) {
    const tipo =
        await festivoRepository
            .obtenerTipoPorId(tipoId);


    if (!tipo) {
        throw new Error(
            "El tipo de festivo no existe"
        );
    }


    const existente =
        await festivoRepository
            .obtenerFestivo(
                tipoId,
                nombre
            );


    if (!existente) {
        throw new Error(
            "El festivo no existe"
        );
    }


    // Combina los datos actuales con los nuevos
    // asi se pueden hacer actualizaciones parciales.
    const datosCompletos = {
        dia:
            datosFestivo.dia !== undefined
                ? datosFestivo.dia
                : existente.dia,

        mes:
            datosFestivo.mes !== undefined
                ? datosFestivo.mes
                : existente.mes,

        nombre:
            datosFestivo.nombre !== undefined
                ? datosFestivo.nombre
                : existente.nombre,

        diasPascua:
            datosFestivo.diasPascua !== undefined
                ? datosFestivo.diasPascua
                : existente.diasPascua
    };


    // Verifica que los datos sigan siendo
    // correctos para el tipo de festivo.
    validarFestivoSegunTipo(
        tipo,
        datosCompletos
    );


    // Si cambia el nombre se verifica
    // que no exista otro festivo con ese nombre.
    if (
        datosFestivo.nombre &&
        datosFestivo.nombre
            .trim()
            .toLowerCase() !==
        nombre
            .trim()
            .toLowerCase()
    ) {

        const nombreDuplicado =
            await festivoRepository
                .obtenerFestivo(
                    tipoId,
                    datosFestivo.nombre
                );


        if (nombreDuplicado) {
            throw new Error(
                "Ya existe un festivo con ese nombre"
            );
        }
    }


    return await festivoRepository
        .actualizarFestivo(
            tipoId,
            nombre,
            datosFestivo
        );
}


// =========================
// ELIMINAR FESTIVO
// =========================
async function eliminarFestivo(
    tipoId,
    nombre
) {
    return await festivoRepository
        .eliminarFestivo(
            tipoId,
            nombre
        );
}


// =========================
// EXPORTACIONES
// =========================
module.exports = {
    listarConfiguracion,
    listarFestivosPorAnio,
    verificarFecha,
    crearFestivo,
    obtenerFestivo,
    actualizarFestivo,
    eliminarFestivo
};