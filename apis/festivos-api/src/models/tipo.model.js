const mongoose = require("mongoose");


// =========================
// ESQUEMA DE FESTIVO
// =========================
const festivoSchema = new mongoose.Schema(
    {
        dia: {
            type: Number,
            required: true
        },

        mes: {
            type: Number,
            required: true
        },

        nombre: {
            type: String,
            required: true,
            trim: true
        },

        diasPascua: {
            type: Number,
            required: false
        }
    },
    {
        // Los festivos están embebidos dentro
        // del documento Tipo y no necesitan
        // un _id propio.
        _id: false
    }
);


// =========================
// ESQUEMA DE TIPO
// =========================
const tipoSchema = new mongoose.Schema(
    {
        id: {
            type: Number,
            required: true,
            unique: true
        },

        tipo: {
            type: String,
            default: ""
        },

        modoCalculo: {
            type: String,
            default: ""
        },

        festivos: {
            type: [festivoSchema],
            default: []
        }
    },
    {
        collection: "tipos",

        
        versionKey: false
    }
);


// =========================
// EXPORTACIÓN
// =========================
module.exports =
    mongoose.model("Tipo", tipoSchema);