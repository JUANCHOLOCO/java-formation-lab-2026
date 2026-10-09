package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;

import lombok.Data;

@Data
public class ProgramadorRutas {

    List<Horario> horarios = new ArrayList<>();

    public void programar(Horario horario) {
        validarCamposObligatorios(horario);
        debeValidarTipoRutasYBuses(horario);
        validarRangoHorario(horario);
        validarSinSolapamiento(horario);
        horarios.add(horario);
    }

    public boolean debeValidarTipoRutasYBuses(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        String tipoBus = horario.getBus().getTipo();
        String tipoRuta = horario.getRuta().getTipo();

        if ("Electric".equals(tipoBus) && !"Electric".equals(tipoRuta)) {
            throw new IllegalArgumentException("Los buses eléctricos solo pueden ir a rutas eléctricas");
        }
        return true;
    }

    private void validarCamposObligatorios(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        validarBus(horario.getBus());
        if (horario.getRuta() == null) {
            throw new IllegalArgumentException("La ruta no puede ser nula");
        }
        if (estaVacio(horario.getRuta().getTipo())) {
            throw new IllegalArgumentException("El tipo de ruta no puede ser nulo o vacío");
        }
        if (horario.getHoraSalida() == null || horario.getHoraLlegada() == null) {
            throw new IllegalArgumentException("Las horas de salida y llegada son obligatorias");
        }
    }

    private void validarBus(Bus bus) {
        if (bus == null) {
            throw new IllegalArgumentException("El bus no puede ser nulo");
        }
        if (estaVacio(bus.getPlaca())) {
            throw new IllegalArgumentException("La placa del bus no puede ser nula o vacía");
        }
        if (estaVacio(bus.getTipo())) {
            throw new IllegalArgumentException("El tipo de bus no puede ser nulo o vacío");
        }
    }

    private static boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private void validarRangoHorario(Horario horario) {
        if (!horario.getHoraLlegada().isAfter(horario.getHoraSalida())) {
            throw new IllegalArgumentException("La hora de llegada debe ser posterior a la de salida");
        }
    }

    private void validarSinSolapamiento(Horario nuevo) {
        boolean solapa = horariosDelBus(nuevo.getBus()).stream()
                .anyMatch(existente -> haySolapamiento(existente, nuevo));
        if (solapa) {
            throw new IllegalArgumentException("El bus ya tiene un horario que se solapa con el indicado");
        }
    }

    private List<Horario> horariosDelBus(Bus bus) {
        return horarios.stream()
                .filter(h -> Objects.equals(h.getBus().getPlaca(), bus.getPlaca()))
                .toList();
    }

    private static boolean haySolapamiento(Horario a, Horario b) {
        return a.getHoraSalida().isBefore(b.getHoraLlegada())
                && b.getHoraSalida().isBefore(a.getHoraLlegada());
    }

}
