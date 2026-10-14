// =========================
// VALIDAR ID DEL TIPO
// =========================
function validarTipoId(req, res, next) {
    const tipoId = Number(req.params.tipoId);

    if (
        !Number.isInteger(tipoId) ||
        tipoId <= 0
    ) {
        return res.status(400).json({
            mensaje:
                "El tipo de festivo no es válido"
        });
    }

    next();
}


// =========================
// VALIDAR AÑO
// =========================
function validarAnio(req, res, next) {
    const anio = Number(req.params.anio);

    if (
        !Number.isInteger(anio) ||
        anio <= 0
    ) {
        return res.status(400).json({
            mensaje:
                "El año no es válido"
        });
    }

    next();
}


// =========================
// VALIDAR FECHA
// =========================
function validarFecha(req, res, next) {
    const anio = Number(req.params.anio);
    const mes = Number(req.params.mes);
    const dia = Number(req.params.dia);


    if (
        !Number.isInteger(anio) ||
        !Number.isInteger(mes) ||
        !Number.isInteger(dia)
    ) {
        return res.status(400).json({
            mensaje:
                "La fecha no es válida"
        });
    }


    if (
        anio <= 0 ||
        mes < 1 ||
        mes > 12 ||
        dia < 1 ||
        dia > 31
    ) {
        return res.status(400).json({
            mensaje:
                "La fecha no es válida"
        });
    }


    // Se crea la fecha y despues se comprueba
    // que JavaScript no haya corregido
    // automáticamente una fecha imposible.
    const fecha = new Date(
        Date.UTC(
            anio,
            mes - 1,
            dia
        )
    );


    const fechaValida =
        fecha.getUTCFullYear() === anio &&
        fecha.getUTCMonth() === mes - 1 &&
        fecha.getUTCDate() === dia;


    if (!fechaValida) {
        return res.status(400).json({
            mensaje:
                "La fecha no es válida"
        });
    }


    next();
}


// =========================
// VALIDAR DATOS DEL FESTIVO
// =========================
// Valida solamente la estructura general.
//
// La interpretación de los datos depende
// del tipo de festivo y se realiza después
// en la logica del servicio.
function validarDatosFestivo(
    req,
    res,
    next
) {
    const {
        dia,
        mes,
        nombre,
        diasPascua
    } = req.body || {};


    // Nombre obligatorio
    if (
        typeof nombre !== "string" ||
        nombre.trim().length === 0
    ) {
        return res.status(400).json({
            mensaje:
                "Debe enviar un nombre válido"
        });
    }


    // Dia obligatorio y entero.
    // Puede ser 0 porque los festivos
    // basados en Pascua usan día 0.
    if (
        dia === undefined ||
        !Number.isInteger(Number(dia)) ||
        Number(dia) < 0 ||
        Number(dia) > 31
    ) {
        return res.status(400).json({
            mensaje:
                "El día debe ser un número entero entre 0 y 31"
        });
    }


    // Mes obligatorio y entero.
    // Puede ser 0 porque los festivos
    // basados en Pascua usan mes 0.
    if (
        mes === undefined ||
        !Number.isInteger(Number(mes)) ||
        Number(mes) < 0 ||
        Number(mes) > 12
    ) {
        return res.status(400).json({
            mensaje:
                "El mes debe ser un número entero entre 0 y 12"
        });
    }


    // diasPascua es opcional porque
    // los tipos 1 y 2 no lo necesitan.
    if (
        diasPascua !== undefined &&
        !Number.isInteger(
            Number(diasPascua)
        )
    ) {
        return res.status(400).json({
            mensaje:
                "diasPascua debe ser un número entero"
        });
    }


    // Normalizar datos antes de continuar.
    req.body.dia = Number(dia);
    req.body.mes = Number(mes);
    req.body.nombre = nombre.trim();

    if (diasPascua !== undefined) {
        req.body.diasPascua =
            Number(diasPascua);
    }


    next();
}


// =========================
// EXPORTACIONES
// =========================
module.exports = {
    validarTipoId,
    validarAnio,
    validarFecha,
    validarDatosFestivo
};