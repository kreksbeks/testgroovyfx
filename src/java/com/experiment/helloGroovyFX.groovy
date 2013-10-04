package com.experiment

import groovy.transform.Field

//@Grab('org.codehaus.groovyfx:groovyfx:0.3.1')

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node
import javafx.scene.SnapshotParameters
import javafx.scene.image.ImageView
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static groovyx.javafx.GroovyFX.start




@Field
ImageView dragImageView = new ImageView()

start {
    final time = new Time()

    def width = 240.0
    def height = 240.0
    def radius = width / 3.0
    def centerX = width / 2.0
    def centerY = height / 2.0


    stage(title: "GroovyFX Clock Demo", width: 512, height: 514, visible: true, resizable: true) {
        scene() {
            fxml resource("/gui.fxml")
//            imageView(id: 'dragImageView')
        }
    }

    primaryStage.scene.lookup('#toolBar').children.each{ i ->
        addGesture(i, primaryStage.scene.lookup('#sceneRoot'))
    }

    initDesignerPane(primaryStage.scene.lookup('#designerPane'))
}

private void addGesture(Node node, Node sceneRoot) {
    node.onDragDetected = { MouseEvent e ->
        SnapshotParameters snapParams = new SnapshotParameters();
        snapParams.fill = Color.TRANSPARENT;
        dragImageView.image = node.snapshot(snapParams, null);

        sceneRoot.getChildren().add(dragImageView);

        dragImageView.startFullDrag();
        e.consume();
    } as EventHandler

    node.onMouseDragged = { MouseEvent e ->
        Point2D localPoint = sceneRoot.sceneToLocal(new Point2D(e.sceneX, e.sceneY));
        dragImageView.relocate(
                (int) (localPoint.getX() - dragImageView.boundsInLocal.width / 2),
                (int) (localPoint.getY() - dragImageView.boundsInLocal.height / 2)
        );
        e.consume();
    } as EventHandler

    node.onMouseEntered = { MouseEvent e ->
            node.setCursor(Cursor.HAND);
        } as EventHandler

    node.onMousePressed = { MouseEvent e ->
        dragItem = node;
        dragImageView.mouseTransparent = true;
        node.mouseTransparent = true;
        node.cursor = Cursor.CLOSED_HAND;
    } as EventHandler

    node.onMouseReleased = { MouseEvent e ->
        dragItem = null;
        dragImageView.mouseTransparent = false;
        node.mouseTransparent = false;
        node.cursor = Cursor.DEFAULT;
        sceneRoot.getChildren().remove(dragImageView);
    } as EventHandler
}

def initDesignerPane(Node designPane) {
    designPane.onMouseDragEntered = { MouseDragEvent e ->
        designPane.style = "-fx-border-color:red;-fx-border-width:2;-fx-border-style:solid;";
        e.consume();
    } as EventHandler;

    designPane.onMouseDragExited = { MouseDragEvent e ->
        designPane.style = "-fx-border-style:none;";
        e.consume();
    } as EventHandler;

    designPane.onMouseDragReleased = { MouseDragEvent e ->
        designPane.children.add(e.source)
        e.consume();
    } as EventHandler;
}
