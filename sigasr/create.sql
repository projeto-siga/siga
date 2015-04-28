create schema CORPORATIVO;
create schema SIGASR;

-- Edson: criei assim apenas pra executar. Falta implementar.
-- Ver http://www.h2database.com/html/features.html#user_defined_functions
CREATE ALIAS REMOVE_ACENTO AS $$
String removeAcento(String comAcento) {
    return comAcento;
}
$$;