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

// =========================
// VALIDAR DÍA Y MES
// =========================
// Comprueba que una combinación día/mes
// pueda existir.
//
// Febrero permite hasta 29 para no impedir
// festivos que puedan existir en años bisiestos.
function validarDiaMes(dia, mes) {

    const diasPorMes = [
        31, // Enero
        29, // Febrero
        31, // Marzo
        30, // Abril
        31, // Mayo
        30, // Junio
        31, // Julio
        31, // Agosto
        30, // Septiembre
        31, // Octubre
        30, // Noviembre
        31  // Diciembre
    ];


    if (
        !Number.isInteger(Number(dia)) ||
        !Number.isInteger(Number(mes))
    ) {
        return false;
    }


    const diaNumero = Number(dia);
    const mesNumero = Number(mes);


    if (
        mesNumero < 1 ||
        mesNumero > 12
    ) {
        return false;
    }


    if (
        diaNumero < 1 ||
        diaNumero >
            diasPorMes[mesNumero - 1]
    ) {
        return false;
    }


    return true;
}

// =========================
// VALIDAR FESTIVO SEGÚN TIPO
// =========================
function validarFestivoSegunTipo(
    tipo,
    datosFestivo
) {

    // =========================
    // TIPO 1 Y TIPO 2
    // =========================
    // Son festivos que parten de
    // una fecha real con día y mes.
    if (
        tipo.id === 1 ||
        tipo.id === 2
    ) {

        if (
            !validarDiaMes(
                datosFestivo.dia,
                datosFestivo.mes
            )
        ) {
            throw new Error(
                "El día y el mes no forman una fecha válida"
            );
        }
    }


    // =========================
    // TIPO 3 Y TIPO 4
    // =========================
    // Son festivos calculados con
    // respecto al Domingo de Pascua.
    if (
        tipo.id === 3 ||
        tipo.id === 4
    ) {

        if (
            datosFestivo.diasPascua ===
            undefined
        ) {
            throw new Error(
                "Este tipo de festivo requiere diasPascua"
            );
        }


        if (
            !Number.isInteger(
                Number(
                    datosFestivo.diasPascua
                )
            )
        ) {
            throw new Error(
                "diasPascua debe ser un número entero"
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