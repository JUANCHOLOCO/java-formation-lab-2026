package com.indra.transporte;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;
import com.indra.transporte.model.Ruta;

public class ProgramadorRutasTest {
    private final ProgramadorRutas programador = new ProgramadorRutas();

    @Test
    @DisplayName("Debe registrar un horario")
    void debeRegistrarUnHorario() {
        Bus bus = new Bus("ABC123", "Diesel");
        Ruta ruta = new Ruta("Electric","R001", "Ciudad A", "Ciudad B");
        Horario horario = new Horario(bus, ruta,
                java.time.LocalTime.of(8, 0), java.time.LocalTime.of(10, 0));

        programador.programar(horario);

        assertEquals(1, programador.getHorarios().size());
    }

    @Nested
    @DisplayName("Cuando el bus es eléctrico")
    class CuandoBusEsElectrico {

        @Test
        @DisplayName("Debe rechazar rutas no eléctricas")
        void debeRechazarRutasNoElectricas() {
            Bus bus = new Bus("ABC123", "Electric");
            Ruta ruta = new Ruta("General", "R001", "Ciudad A", "Ciudad B");
            Horario horario = new Horario(bus, ruta,
                    java.time.LocalTime.of(8, 0), java.time.LocalTime.of(10, 0));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                programador.debeValidarTipoRutasYBuses(horario);
            });

            assertEquals("Los buses eléctricos solo pueden ir a rutas eléctricas", exception.getMessage());
        }

        @Test
        @DisplayName("Debe permitir rutas eléctricas")
        void debePermitirRutasElectricas() {
            Bus bus = new Bus("ABC123", "Electric");
            Ruta ruta = new Ruta("Electric", "R001", "Ciudad A", "Ciudad B");
            Horario horario = new Horario(bus, ruta,
                    java.time.LocalTime.of(8, 0), java.time.LocalTime.of(10, 0));

            assertDoesNotThrow(() -> programador.debeValidarTipoRutasYBuses(horario));
        }
    }

    @Nested
    @DisplayName("Cuando el bus no es eléctrico")
    class CuandoBusNoEsElectrico {

        @Test
        @DisplayName("Debe permitir cualquier tipo de ruta")
        void debePermitirCualquierTipoDeRuta() {
            Bus bus = new Bus("ABC123", "Diesel");
            Ruta ruta = new Ruta("General", "R001", "Ciudad A", "Ciudad B");
            Horario horario = new Horario(bus, ruta,
                    java.time.LocalTime.of(8, 0), java.time.LocalTime.of(10, 0));

            assertDoesNotThrow(() -> programador.debeValidarTipoRutasYBuses(horario));
        }
    }

    @Test
    @DisplayName("Debe devolver los horarios del tipo solicitado")
    void debeDevolverLosHorariosDelTipoSolicitado() {
        fail("Implementar este test para devolver los horarios del tipo solicitado");
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el bus es desconocido")
    void debeLanzarIllegalArgumentExceptionCuandoBusEsDesconocido() {
        fail("Implementar este test para lanzar IllegalArgumentException cuando el bus es desconocido");
    }

    @Test
    @DisplayName("Debe lanzar UnsupportedTypeException cuando el tipo es desconocido")
    void debeLanzarUnsupportedTypeExceptionCuandoTipoEsDesconocido() {
        fail("Implementar este test para lanzar UnsupportedTypeException cuando el tipo es desconocido");
    }

    @Test
    @DisplayName("Debe rechazar un horario nulo")
    void debeRechazarHorarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> programador.programar(null));
    }

    @Test
    @DisplayName("Debe rechazar un horario sin bus")
    void debeRechazarBusNulo() {
        Horario sinBus = horario(null, ruta(GENERAL), hora("08:00"), hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(sinBus));
    }

    @Test
    @DisplayName("Debe rechazar un horario sin ruta")
    void debeRechazarRutaNula() {
        Horario sinRuta = horario(bus(PLACA, DIESEL), null, hora("08:00"), hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(sinRuta));
    }

    @Test
    @DisplayName("Debe rechazar un horario sin hora de salida")
    void debeRechazarHoraSalidaNula() {
        Horario sinSalida = horario(bus(PLACA, DIESEL), ruta(GENERAL), null, hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(sinSalida));
    }

    @Test
    @DisplayName("Debe rechazar un horario sin hora de llegada")
    void debeRechazarHoraLlegadaNula() {
        Horario sinLlegada = horario(bus(PLACA, DIESEL), ruta(GENERAL), hora("08:00"), null);
        assertThrows(IllegalArgumentException.class, () -> programador.programar(sinLlegada));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Debe rechazar una placa de bus nula, vacía o en blanco")
    void debeRechazarPlacaNulaOVacia(String placa) {
        Horario h = horario(bus(placa, DIESEL), ruta(GENERAL), hora("08:00"), hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(h));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Debe rechazar un tipo de bus nulo, vacío o en blanco")
    void debeRechazarTipoBusNuloOVacio(String tipoBus) {
        Horario h = horario(bus(PLACA, tipoBus), ruta(GENERAL), hora("08:00"), hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(h));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Debe rechazar un tipo de ruta nulo, vacío o en blanco")
    void debeRechazarTipoRutaNuloOVacio(String tipoRuta) {
        Horario h = horario(bus(PLACA, DIESEL), ruta(tipoRuta), hora("08:00"), hora("10:00"));
        assertThrows(IllegalArgumentException.class, () -> programador.programar(h));
    }
}
