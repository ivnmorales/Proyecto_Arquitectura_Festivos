const express = require("express");

const festivoController =
    require("../controllers/festivo.controller");

const festivoValidator =
    require("../middleware/festivo.validator");


const router = express.Router();


/**
 * @swagger
 * /api/festivos/:
 *   get:
 *     summary: Listar configuración de festivos
 *     description: Obtiene los tipos y festivos almacenados en MongoDB.
 *     tags:
 *       - Festivos
 *     responses:
 *       200:
 *         description: Configuración obtenida correctamente
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Tipo'
 *       500:
 *         description: Error interno del servidor
 */
router.get(
    "/",
    festivoController.listarConfiguracion
);


/**
 * @swagger
 * /api/festivos/verificar/{anio}/{mes}/{dia}:
 *   get:
 *     summary: Verificar si una fecha es festiva
 *     description: Verifica si una fecha corresponde a uno o varios festivos.
 *     tags:
 *       - Festivos
 *     parameters:
 *       - in: path
 *         name: anio
 *         required: true
 *         schema:
 *           type: integer
 *         example: 2026
 *       - in: path
 *         name: mes
 *         required: true
 *         schema:
 *           type: integer
 *         example: 12
 *       - in: path
 *         name: dia
 *         required: true
 *         schema:
 *           type: integer
 *         example: 25
 *     responses:
 *       200:
 *         description: Resultado de la verificación
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/VerificacionFecha'
 *       400:
 *         description: Fecha inválida
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 */
router.get(
    "/verificar/:anio/:mes/:dia",
    festivoValidator.validarFecha,
    festivoController.verificarFecha
);


/**
 * @swagger
 * /api/festivos/obtener/{anio}:
 *   get:
 *     summary: Obtener festivos de un año
 *     description: Calcula todos los festivos del año solicitado usando la configuración almacenada en MongoDB.
 *     tags:
 *       - Festivos
 *     parameters:
 *       - in: path
 *         name: anio
 *         required: true
 *         schema:
 *           type: integer
 *         example: 2026
 *     responses:
 *       200:
 *         description: Festivos calculados correctamente
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/FestivoCalculado'
 *       400:
 *         description: Año inválido
 */
router.get(
    "/obtener/:anio",
    festivoValidator.validarAnio,
    festivoController.listarPorAnio
);


/**
 * @swagger
 * /api/festivos/tipo/{tipoId}/festivo:
 *   post:
 *     summary: Crear un nuevo festivo
 *     description: Agrega un nuevo festivo al tipo indicado.
 *     tags:
 *       - CRUD Festivos
 *     parameters:
 *       - in: path
 *         name: tipoId
 *         required: true
 *         schema:
 *           type: integer
 *         description: Tipo de regla de cálculo del festivo
 *         example: 2
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Festivo'
 *           example:
 *             dia: 9
 *             mes: 7
 *             nombre: "Festivo de prueba"
 *     responses:
 *       201:
 *         description: Festivo creado correctamente
 *       400:
 *         description: Datos inválidos o festivo duplicado
 */
router.post(
    "/tipo/:tipoId/festivo",
    festivoValidator.validarTipoId,
    festivoValidator.validarDatosFestivo,
    festivoController.crearFestivo
);


/**
 * @swagger
 * /api/festivos/tipo/{tipoId}/festivo/{nombre}:
 *   get:
 *     summary: Obtener un festivo específico
 *     tags:
 *       - CRUD Festivos
 *     parameters:
 *       - in: path
 *         name: tipoId
 *         required: true
 *         schema:
 *           type: integer
 *         example: 1
 *       - in: path
 *         name: nombre
 *         required: true
 *         schema:
 *           type: string
 *         example: Navidad
 *     responses:
 *       200:
 *         description: Festivo encontrado
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Festivo'
 *       404:
 *         description: Festivo no encontrado
 */
router.get(
    "/tipo/:tipoId/festivo/:nombre",
    festivoValidator.validarTipoId,
    festivoController.obtenerFestivo
);


/**
 * @swagger
 * /api/festivos/tipo/{tipoId}/festivo/{nombre}:
 *   put:
 *     summary: Actualizar un festivo
 *     tags:
 *       - CRUD Festivos
 *     parameters:
 *       - in: path
 *         name: tipoId
 *         required: true
 *         schema:
 *           type: integer
 *         example: 2
 *       - in: path
 *         name: nombre
 *         required: true
 *         schema:
 *           type: string
 *         example: Festivo de prueba
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Festivo'
 *           example:
 *             dia: 10
 *             mes: 7
 *             nombre: "Festivo actualizado"
 *     responses:
 *       200:
 *         description: Festivo actualizado correctamente
 *       400:
 *         description: Datos inválidos
 *       404:
 *         description: Festivo no encontrado
 */
router.put(
    "/tipo/:tipoId/festivo/:nombre",
    festivoValidator.validarTipoId,
    festivoValidator.validarDatosFestivo,
    festivoController.actualizarFestivo
);


/**
 * @swagger
 * /api/festivos/tipo/{tipoId}/festivo/{nombre}:
 *   delete:
 *     summary: Eliminar un festivo
 *     tags:
 *       - CRUD Festivos
 *     parameters:
 *       - in: path
 *         name: tipoId
 *         required: true
 *         schema:
 *           type: integer
 *         example: 2
 *       - in: path
 *         name: nombre
 *         required: true
 *         schema:
 *           type: string
 *         example: Festivo actualizado
 *     responses:
 *       200:
 *         description: Festivo eliminado correctamente
 *       404:
 *         description: Festivo no encontrado
 */
router.delete(
    "/tipo/:tipoId/festivo/:nombre",
    festivoValidator.validarTipoId,
    festivoController.eliminarFestivo
);


module.exports = router;