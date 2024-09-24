Feature: Operaciones Delete de Usuarios
  Como usuario de la API
  Quiero gestionar usuarios (crear, leer, actualizar, eliminar) And realizar autenticación
  Para poder mantener los datos de los usuarios And asegurar el acceso
  
  Scenario: Eliminación exitosa de usuario
    Given Un usuario autenticado
    And intenta eliminarse a si mismo
    When se envía una solicitud de eliminacion con el usuario
    Then el estado de la respuesta debe ser 200
    And el mensaje de respuesta debe ser "Usuario eliminado"

  Scenario: Fallo al eliminar un usuario
    Given Un usuario autenticado
    And intenta eliminar otro usuario
    When se envía una solicitud de eliminacion con el usuario
    Then el estado de la respuesta debe ser 401
    And el mensaje de error debe ser "Error: Usuario no autorizado"

  Scenario: Fallo al eliminar un usuario no existente
    Given Un usuario autenticado
    And intenta eliminar un usuario no existente
    When se envía una solicitud de eliminacion con el usuario
    Then el estado de la respuesta debe ser 404
    And el mensaje de error debe ser "El usuario a eliminar no fue encontrado"

  Scenario: Fallo al eliminar un usuario existente
    Given Un usuario no autenticado
    And intenta eliminar otro usuario
    When se envía una solicitud de eliminacion con el usuario
    Then el estado de la respuesta debe ser 401
    And el mensaje de error debe ser "Error: Usuario no autorizado"