package org.example.annotation;

import org.example.figure.Point;

public class AnnotatedImage {

    private final String imagePath;

    private final Annotation[] annotations;

    public Annotation findByPoint(int x, int y) {
        for (Annotation annotation : annotations) {
            if (annotation.getFigure().isPointInside(new Point(x, y))) {
                return annotation;
            }
        }
        return null;
    }

    public Annotation findByLabel(String label) {
        for (Annotation annotation : annotations) {
            if (annotation.getSignature().contains(label)) {
                return annotation;
            }
        }
        return null;
    }

    public AnnotatedImage(String imagePath, Annotation... annotations) {
        this.imagePath = imagePath;
        this.annotations = annotations;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }
}

