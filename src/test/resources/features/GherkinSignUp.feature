Feature: Operaciones CRUD de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) AND realizar autenticación
  Para poder mantener los datos de los usuarios AND asegurar el acceso

  Background:
    GIVEN que el servidor está en ejecución

   Scenario: Registro de usuario exitoso
    GIVEN un nuevo usuario con detalles válidos
    WHEN se envía una solicitud de registro con los datos de ese usuario
    THEN el estado de la respuesta debe ser 200
    AND el mensaje de respuesta debe ser "Usuario creado"

  Scenario: Registro de usuario invalido
    GIVEN un nuevo usuario con detalles invalidos
    WHEN se envía una solicitud de registro con los datos de ese usuario
    THEN el estado de la respuesta debe ser 400
    AND el mensaje de respuesta debe ser "Error al crear el usuario"