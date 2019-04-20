package br.edu.ufabc.ipj.spaceshooter.core.spaceshipselection;

import br.edu.ufabc.ipj.spaceshooter.SpaceShooterGame;
import br.edu.ufabc.ipj.spaceshooter.model.AbstractModel;
import br.edu.ufabc.ipj.spaceshooter.model.Pedestal;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCargoSarship;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiCosair;
import br.edu.ufabc.ipj.spaceshooter.model.SciFiFighter;
import br.edu.ufabc.ipj.spaceshooter.utils.Commands;
import br.edu.ufabc.ipj.spaceshooter.utils.ModelSelector;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

public class SpaceShipSelectionAction {
    
    public ModelSelector currentSelection;
    
    private boolean hadCommand;
    
    protected Array<AbstractModel> objects;
    
    public SpaceShipSelectionAction(){
        hadCommand = false;
        
        currentSelection = ModelSelector.SCIFI_FIGHTER;
        
        objects = new Array<AbstractModel>();
        
        //Load models in here
        objects.add(new Pedestal());
        objects.add(new SciFiFighter());
        objects.add(new SciFiCosair(false));
        objects.add(new SciFiCargoSarship(false));
        
        for (AbstractModel obj: objects){
            for (Material mat : obj.getGameObject().materials)
                mat.remove(ColorAttribute.Emissive);
            if (!(obj instanceof Pedestal))
                obj.getGameObject().transform.translate(0, 8 / obj.getGameObject().transform.getScaleY(), 0);
        }
    }
    
    public void update(float delta){
        for (AbstractModel o : objects)
            o.update(delta);
        
        if (!hadCommand && Commands.set[Commands.Command.LEFT.getValue()])
            swapSpaceship(Commands.Command.LEFT);
        else if (!hadCommand && Commands.set[Commands.Command.RIGHT.getValue()])
            swapSpaceship(Commands.Command.RIGHT);
        else hadCommand = Commands.hasCommand();
        
        for (AbstractModel o : objects)
            if (o instanceof SciFiFighter)
                ((SciFiFighter) o).rotateSelection(delta);
            else if (o instanceof SciFiCosair)
                ((SciFiCosair) o).rotateSelection(delta);
            else if (o instanceof SciFiCargoSarship)
                ((SciFiCargoSarship) o).rotateSelection(delta);
        
        SpaceShooterGame.DEBUG = Commands.set[Commands.Command.DEBUG.getValue()];
    }
    
    private void swapSpaceship(Commands.Command command){
        hadCommand = true;
        
        int i;
        for (i = 1; i < objects.size; ++i)
            if (objects.get(i).getGameObject().isVisible()) break;
        
        objects.get(i).getGameObject().setVisible(false);
        if (command == Commands.Command.RIGHT) currentSelection = ModelSelector.fromInteger(++i >= objects.size ? 1 : i);
        else currentSelection = ModelSelector.fromInteger(--i == 0 ? objects.size - 1 : i);
        objects.get(currentSelection.getValue()).getGameObject().setVisible(true);
    }
}