package Sprite;

import Romandefense.core.GameScreen;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.World;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import playn.core.Layer;
import playn.core.util.Callback;
import playn.core.util.Clock;



public class Minutor {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;

    public Minutor(final World world, final float x,final float y){
        sprite = SpriteLoader.getSprite("images/minutor.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {

                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y);


                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x,
                        GameScreen.M_PER_PIXEL * y);

                hasLoaded = true;

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }
    private Body initPhysicsBody(World world,float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);


        ///EdgeShape shape = new EdgeShape();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(96 * GameScreen.M_PER_PIXEL /2, sprite.layer().height()* GameScreen.M_PER_PIXEL /2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);

        return body;
    }

    public Layer layer(){
        return sprite.layer();
    }

    public void paint(Clock clock) {
        if (!hasLoaded)
            return;

        sprite.layer().setTranslation((body.getPosition().x/GameScreen.M_PER_PIXEL),
                (body.getPosition().y/GameScreen.M_PER_PIXEL));

        sprite.layer().setRotation(body.getAngle());

    }

    public enum State{
        IDLE
    }

    private State state=State.IDLE;
    private int offset=0;
    private int e=0;



    public void update(int delta){
        if (!hasLoaded) return;
        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE: offset =0;
                    break;
            }
            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e=0;

        }
    }



}
