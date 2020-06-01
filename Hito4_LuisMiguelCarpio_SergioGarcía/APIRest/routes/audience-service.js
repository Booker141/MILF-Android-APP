'use strict';

const MongoClient = require('mongodb').MongoClient;
let db;	//Variable que contendra la BD
let ObjectId = require('mongodb').ObjectID;
const Audience = function () {};	//Definicion de funcion Audience

Audience.prototype.connectDb = function (callback) {
	MongoClient.connect("mongodb+srv://admin:admin@probandorest-4fkhk.mongodb.net/test?retryWrites=true&w=majority", {	//Conexion a la BD mediante el API Key de mongo
		useNewUrlParser: true,
		useUnifiedTopology: true
	}, function (err, database) {
		if (err) {
			callback(err);
		}
		db = database.db('ProbandoREST').collection('audience');	//Especificamos la coleccion
		callback(err, database);
	});
};

//Devuelve todos los elementos de la coleccion
Audience.prototype.getAll = function (callback) {
	return db.find({}).project({Nombre:1, Apellidos:1}).toArray(callback);
};

//Devuelve el elemento representado por el ID indicado
Audience.prototype.get = function (_id, callback) {
	return db.find({_id: ObjectId(_id)}).toArray(callback);
};

//Añade a la coleccion el elemento indicado
Audience.prototype.add = function (person, callback) {
	return db.insertOne(person, callback);
};

//Modifica el elemento representado por el ID indicado con los datos indicados
Audience.prototype.update = function (_id, updatedPerson, callback) {
	delete updatedPerson._id;
	return db.updateOne({_id: ObjectId(_id)}, {$set: updatedPerson}, callback);
};

//Elimina todos los elementos de la coleccion
Audience.prototype.removeAll = function (callback) {
	return db.deleteMany({}, callback);
};

//Elimina el elemento representado por el ID indicado
Audience.prototype.remove = function (_id, callback) {
	return db.deleteOne({_id: ObjectId(_id)}, callback);
};

module.exports = new Audience();