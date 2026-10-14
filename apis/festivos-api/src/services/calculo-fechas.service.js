// =========================
// CREAR FECHA
// =========================

function crearFecha(anio, mes, dia) {
    return new Date(
        Date.UTC(anio, mes - 1, dia)
    );
}


// =========================
// AGREGAR DÍAS
// =========================

function agregarDias(fecha, dias) {
    const nuevaFecha = new Date(fecha);

    nuevaFecha.setUTCDate(
        nuevaFecha.getUTCDate() + dias
    );

    return nuevaFecha;
}


// =========================
// SIGUIENTE LUNES
// =========================

function siguienteLunes(fecha) {
    const nuevaFecha = new Date(fecha);

    const diaSemana =
        nuevaFecha.getUTCDay();

    let diasParaLunes = 0;

    if (diaSemana !== 1) {
        diasParaLunes =
            1 + (7 - diaSemana) % 7;
    }

    return agregarDias(
        nuevaFecha,
        diasParaLunes
    );
}


// =========================
// OBTENER DOMINGO DE RAMOS
// =========================

function obtenerDomingoRamos(anio) {
    const a = anio % 19;
    const b = anio % 4;
    const c = anio % 7;

    const d =
        (19 * a + 24) % 30;

    const dias =
        d +
        (
            2 * b +
            4 * c +
            6 * d +
            5
        ) % 7;

    const marzo15 =
        crearFecha(
            anio,
            3,
            15
        );

    return agregarDias(
        marzo15,
        dias
    );
}


// =========================
// OBTENER DOMINGO DE PASCUA
// =========================

function calcularDomingoPascua(anio) {
    const domingoRamos =
        obtenerDomingoRamos(anio);

    return agregarDias(
        domingoRamos,
        7
    );
}


// =========================
// FORMATEAR FECHA
// =========================

function formatearFecha(fecha) {
    return fecha
        .toISOString()
        .substring(0, 10);
}


// =========================
// CALCULAR FESTIVO
// =========================

function calcularFestivo(
    tipo,
    festivo,
    anio
) {
    let fecha;


    // =========================
    // TIPO 1
    // FECHA FIJA
    // =========================
    if (tipo.id === 1) {

        fecha = crearFecha(
            anio,
            festivo.mes,
            festivo.dia
        );
    }


    // =========================
    // TIPO 2
    // PUENTE FESTIVO
    // =========================
    else if (tipo.id === 2) {

        const fechaOriginal =
            crearFecha(
                anio,
                festivo.mes,
                festivo.dia
            );

        fecha =
            siguienteLunes(
                fechaOriginal
            );
    }


    // =========================
    // TIPO 3
    // BASADO EN PASCUA
    // =========================
    else if (tipo.id === 3) {

        const domingoPascua =
            calcularDomingoPascua(
                anio
            );

        fecha =
            agregarDias(
                domingoPascua,
                festivo.diasPascua
            );
    }


    // =========================
    // TIPO 4
    // PASCUA + PUENTE FESTIVO
    // =========================
    else if (tipo.id === 4) {

        const domingoPascua =
            calcularDomingoPascua(
                anio
            );

        const fechaCalculada =
            agregarDias(
                domingoPascua,
                festivo.diasPascua
            );

        fecha =
            siguienteLunes(
                fechaCalculada
            );
    }


    // =========================
    // TIPO NO SOPORTADO
    // =========================
    else {
        throw new Error(
            `Tipo de festivo no soportado: ${tipo.id}`
        );
    }


    return {
        nombre: festivo.nombre,
        fecha: formatearFecha(fecha)
    };
}


// =========================
// CALCULAR FESTIVOS DEL AÑO
// =========================

function calcularFestivosAnio(
    tipos,
    anio
) {
    const festivosCalculados = [];

    for (const tipo of tipos) {

        if (!Array.isArray(tipo.festivos)) {
            continue;
        }

        for (const festivo of tipo.festivos) {

            const festivoCalculado =
                calcularFestivo(
                    tipo,
                    festivo,
                    anio
                );

            festivosCalculados.push(
                festivoCalculado
            );
        }
    }


   
    festivosCalculados.sort(
        (a, b) =>
            a.fecha.localeCompare(b.fecha)
    );


    return festivosCalculados;
}


// =========================
// EXPORTACIONES
// =========================
module.exports = {
    crearFecha,
    agregarDias,
    siguienteLunes,
    obtenerDomingoRamos,
    calcularDomingoPascua,
    calcularFestivo,
    calcularFestivosAnio
};