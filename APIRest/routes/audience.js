'use strict';

const express = require('express');
const router = express.Router(); //Permite la ramificacion de los servicios
const audienceService = require('./audience-service');

//Devolucion de getAll considerando posibles errores y coleccion vacia
router.get('/', function (req, res) {
	audienceService.getAll((err, audience) => {
		if(err){
			res.status(500).send({msg: err});
		} else if (audience === null){
			res.status(500).send({msg: "No hay asistentes"});
		} else {
			res.status(200).send(audience);
		}
	});
});

//Devolucion de get considerando posibles errores y elemento inexistente
router.get('/:_id', function (req, res) {
	let _id = req.params._id;
	audienceService.get(_id, (err, person) => {
		if(err){
			res.status(500).send({msg: err});
		} else if(person === null){
			res.status(500).send({msg: 'No existe dicho asistente'});
		} else {
			res.status(200).send(person);
		}
	});
});

//Devolucion de add considerando posibles errores y elemento no vacio
router.post('/', function (req, res) {
	let person = req.body;
	audienceService.add(person, (err, person) => {
		if(err){
			res.status(500).send({msg: err});
		} else if(person !== null){
			res.send({msg: 'Asistente añadido'});
		}
	});
});

//Devolucion de update considerando posibles errores y no actualizacion
router.put('/:_id', function (req, res) {
	const _id = req.params._id;
	const updatedPerson = req.body;
	audienceService.update(dni, updatedPerson, (err, numUpdates) => {
		if(err || numUpdates === 0) {
			res.status(500).send({msg: err});
		} else {
			res.status(200).send({msg: 'Asistente modificado'});
		}
	});
});

//Devolucion de removeAll considerando posibles errores
router.delete('/', function (req, res) {
	audienceService.removeAll((err) => {
		if(err){
			res.status(500).send({msg: err});
		} else {
			res.status(200).send({msg: 'Se han eliminado todos los asistentes'});
		}
	});
});

//Devolucion de remove considerando posibles errores
router.delete('/:_id', function (req, res) {
	let _id = req.params._id;
	audienceService.remove(_id, (err) => {
		if(err){
			res.status(404).send({msg: err});
		} else {
			res.status(200).send({msg: 'Asistente eliminado'});
		}
	});
});

module.exports = router;