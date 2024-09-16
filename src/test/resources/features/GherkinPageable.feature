Feature: Operaciones CRUD de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) AND realizar autenticación
  Para poder mantener los datos de los usuarios AND asegurar el acceso

  Background:
    GIVEN que el servidor está en ejecución

  Scenario: Obtención de usuarios paginados
    GIVEN Hay usuarios en la base de datos
    AND se dan valores positivos para "numeroPagina" y "tamanoPagina"
    WHEN se envía una solicitud de paginacion con "numeroPagina" y "tamanoPagina"
    THEN el estado de la respuesta debe ser 200
    AND la respuesta debe contener una lista de usuarios con detalles de paginación

  Scenario: Obtención de usuarios paginados
    GIVEN Hay usuarios en la base de datos
    AND se dan valores negativos para "numeroPagina" y "tamanoPagina"
    WHEN se envía una solicitud de paginacion con "numeroPagina" y "tamanoPagina"
    THEN el estado de la respuesta debe ser 400
    AND el mensaje de respuesta debe ser "No se pueden dar valores negativos"

  Scenario: Fallo al obtener usuarios paginados
    GIVEN No hay usuarios en la base de datos
    WHEN se envía una solicitud de paginacion con "numeroPagina" y "tamanoPagina"
    THEN el estado de la respuesta debe ser 400
    AND el mensaje de respuesta debe ser "Error al obtener los usuarios"
