import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Blackjack {

    public static void main(String[] args) {
        Scanner escaneo = new Scanner(System.in);

        System.out.println("=== BIENVENIDO AL BLACKJACK ===");
        System.out.print("Introduce la cantidad de dinero inicial: ");
        double dineroJugador = escaneo.nextDouble();

        while (dineroJugador > 0) {
            System.out.println("\n-----------------------------------");
            System.out.println("Dinero actual: " + dineroJugador + "€");

            double apuesta = 0;
            while (apuesta <= 0 || apuesta > dineroJugador) {
                System.out.print("¿Cuánto quieres apostar?: ");
                apuesta = escaneo.nextDouble();
                if (apuesta <= 0 || apuesta > dineroJugador) {
                    System.out.println("Apuesta no válida. Revisa tu saldo disponible.");
                }
            }

            ArrayList<Double> baraja = crearBaraja();
            Collections.shuffle(baraja);

            ArrayList<Double> manoJugador = new ArrayList<>();
            ArrayList<Double> manoCrupier = new ArrayList<>();

            manoJugador.add(baraja.remove(0));
            manoJugador.add(baraja.remove(0));
            manoCrupier.add(baraja.remove(0));
            manoCrupier.add(baraja.remove(0));

            boolean turnoJugador = true;
            boolean seHaPasado = false;

            while (turnoJugador) {
                System.out.println("\nMano del crupier: " + obtenerNombreCarta(manoCrupier.get(0)) + " y [Carta Oculta]");
                mostrarMano("Tu mano", manoJugador);

                int puntuacionJugador = calcularPuntuacion(manoJugador);

                if(puntuacionJugador == 7.5) {
                    System.out.println("¡Tienes 7.5! ¡Ganas la ronda!");
                    dineroJugador += apuesta;
                    turnoJugador = false;

                } else if(puntuacionJugador > 10) {
                    System.out.println("¡Te has pasado de 10!");
                    seHaPasado = true;
                    turnoJugador = false;






                } else if (puntuacionJugador == 10) {
                    System.out.println("¡Tienes 10!");
                    turnoJugador = false;
                } else {
                    System.out.print("¿Qué deseas hacer? (1: Pedir carta / Hit, 2: Plantarse / Stand): ");
                    int opcion = escaneo.nextInt();

                    if (opcion == 1) {
                        manoJugador.add(baraja.remove(0));
                    } else if (opcion == 2) {
                        turnoJugador = false;
                    } else {
                        System.out.println("Opción no válida. Elige 1 o 2.");
                    }
                }
            }

            int puntuacionJugador = calcularPuntuacion(manoJugador);

            if (!seHaPasado) {
                System.out.println("\n--- Turno del Crupier ---");
                mostrarMano("Mano completa del crupier", manoCrupier);

                while (calcularPuntuacion(manoCrupier) < 17) {
                    double nuevaCarta = baraja.remove(0);
                    manoCrupier.add(nuevaCarta);
                    System.out.println("El crupier roba: " + obtenerNombreCarta(nuevaCarta));
                }

                mostrarMano("Mano final del crupier", manoCrupier);
            }

            int puntuacionCrupier = calcularPuntuacion(manoCrupier);

            System.out.println("\n--- Resultado de la ronda ---");
            if (seHaPasado) {
                System.out.println("Has perdido la ronda.");
                dineroJugador -= apuesta;
            } else if (puntuacionCrupier > 21) {
                System.out.println("¡El crupier se ha pasado! ¡Ganas la ronda!");
                dineroJugador += apuesta;
            } else if (puntuacionJugador > puntuacionCrupier) {
                System.out.println("¡Tu puntuación es mayor! ¡Ganas la ronda!");
                dineroJugador += apuesta;
            } else if (puntuacionCrupier > puntuacionJugador) {
                System.out.println("La puntuación del crupier es mayor. Has perdido.");
                dineroJugador -= apuesta;
            } else {
                System.out.println("Empate. Se devuelve tu apuesta.");
            }

            if (dineroJugador <= 0) {
                System.out.println("\nTe has quedado sin dinero. Fin del juego.");
                break;
            }

            System.out.print("\n¿Quieres seguir jugando? (1: Sí, 2: No): ");
            int continuar = escaneo.nextInt();
            if (continuar != 1) {
                System.out.println("¡Gracias por jugar! Te retiras con " + dineroJugador + "€");
                break;
            }
        }

        escaneo.close();
    }

    public static ArrayList<Double> crearBaraja() {
        ArrayList<Double> baraja = new ArrayList<>();
        for (int palo = 0; palo < 4; palo++) {
            for (int valor = 1; valor <= 13; valor++) {
                if (valor == 11 || valor == 12 || valor == 13) {

                    baraja.add(0.5); // Añadir Jota, K y Q
                    
                }
                baraja.add((double) valor);           
            }
        }
        return baraja;
    }

    public static int calcularPuntuacion(ArrayList<Double> mano) {
        int puntuacion = 0;
        int cantidadAses = 0;

        for (int i = 0; i < mano.size(); i++) {
            double carta = mano.get(i);
            if (carta == 1) {
                cantidadAses++;
                puntuacion += 11;
            } else if (carta >= 10) {
                puntuacion += 10;
            } else {
                puntuacion += carta;
            }
        }

        while (puntuacion > 21 && cantidadAses > 0) {
            puntuacion -= 10;
            cantidadAses--;
        }

        return puntuacion;
    }

    public static String obtenerNombreCarta(double carta) {
        if (carta == 1) {
            return "As";
        } else if (carta == 11) {
            return "Jota";
        } else if (carta == 12) {
            return "Reina";
        } else if (carta == 13) {
            return "Rey";
        } else {
            return String.valueOf(carta);
        }
    }

    public static void mostrarMano(String nombreJugador, ArrayList<Double> mano) {
        System.out.print(nombreJugador + ": ");
        for (int i = 0; i < mano.size(); i++) {
            System.out.print(obtenerNombreCarta(mano.get(i)) + " ");
        }
        System.out.println("(Puntuación: " + calcularPuntuacion(mano) + ")");
    }
}