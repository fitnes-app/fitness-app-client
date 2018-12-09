package com.fitnessapp.client.Utils;

public class QuestionLibrary {

    private String mQuestions [] = {
            "Mis hombros son:",
            "Unos pantalones normales me quedan:",
            "Mis antebrazos son:",
            "Mi cuerpo tiende a:",
            "Mi cuerpo, en general, tiende a ser:",
            "Si intento rodear mi muñeca con la otra mano:",
            "Medidas del pecho:"
    };

    private String mChoices [][] = {
            {"Más anchos que mis caderas", "Iguales que mis caderas", "Más estrechos que mis caderas"},
            {"Apretados a mis glúteos", "Perfectos en mis glúteos", "Largos en mis glúteos"},
            {"Grandes", "Medios", "Pequeños"},
            {"Engordar fácilmente", "Mantenerse", "Adelgazar fácilmente"},
            {"Redondo y suave", "Cuadrado y rugoso", "Largo y estrecho"},
            {"El pulgar y el dedo corazón no se tocan", "El pulgar y el dedo corazón justo se tocan", "El pulgar y el dedo corazón se sobrepasan"},
            {"110 cm o más", "Entre 93 y 110 cm", "93 cm o menos"}
    };


    private String mEndomorphAnswers[] = {"Más anchos que mis caderas", "Largos en mis glúteos", "Grandes",
            "Engordar fácilmente", "Redondo y suave", "El pulgar y el dedo corazón no se tocan", "110 cm o más"};

    private String mEctomorphAnswers[] = {"Más estrechos que mis caderas", "Apretados aT mis glúteos", "Pequeños",
            "Adelgazar fácilmente", "Largo y estrecho", "El pulgar y el dedo corazón se sobrepasan", "93 cm o menos"};

    private String mMesomorphAnswers[] = {"Iguales que mis caderas", "Perfectos en mis glúteos", "Medios",
            "Mantenerse", "Cuadrado y rugoso", "El pulgar y el dedo corazón justo se tocan", "Entre 93 y 110 cm"};


    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }


    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }

    public String getChoice3(int a) {
        String choice2 = mChoices[a][2];
        return choice2;
    }

    public String getAnswerEcto(int a) {
        String answer = mEctomorphAnswers[a];
        return answer;
    }

    public String getAnswerMeso(int a) {
        String answer = mMesomorphAnswers[a];
        return answer;
    }

    public String getAnswerEndo(int a) {
        String answer = mEndomorphAnswers[a];
        return answer;
    }
}