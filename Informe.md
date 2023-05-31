#Informe
**Integrantes**
- Jose Alejandro García Marcos — A00365583.
- Juan Esteban Caicedo Alzate — A00365977.
- Juan Pablo Ramos Torres— A00368429.
##Cómo hicimos el programa
Tomamos un proyecto que creaba una plataforma de venta y forma de computadores y lo adaptamos para que tuviera las necesidades que se nos pedía en el enunciado. Así, añadimos los siguientes cambios.
- El administrador ya no puede cambiar el rol de un usuario básico a administrador.
- El admin puede eliminar usuarios básicos.
- El admin puede borrar la contraseña de un usuario.
- Los usuarios básicos pueden ver su última fecha y hora de inicio de sesión.
- Los usuarios básicos pueden cambiar su contraseña.
- Las contraseñas se almacenan con salt, a través del algoritmo PBKDF2 par el hashing.
##Dificultades
Nos hubiera gustado usar una librería o API para realizar el cambio de contraseña de modo que se enviara un correo o algo parecido al usuario para que siguiera un enlace y estableciera su contraseña pero, por cuestiones de tiempo, adoptamos un mecanismo más manual y humano en el que el administrador provee una contraseña provisional y por canales externos ajenos al sistema, le informa al usuario de la situación, le da su contraseña y le pide que la actualice lo antes posible.
También quisimos implementar Spring Security pero nos fue imposible adaptar el proyecto.
##Conclusiones
Nos alegra ver que algoritmos tan importantes y útiles como el de PBKDF2 están disponibles abiertamente (de forma fácil de adoptar) y aún así sean seguros.

