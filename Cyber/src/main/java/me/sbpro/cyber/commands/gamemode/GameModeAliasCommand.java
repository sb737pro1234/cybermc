package me.sbpro.cyber.commands.gamemode;

public abstract class GameModeAliasCommand extends GameModeCommand {

    private final GamemodeType gamemode;

    public GameModeAliasCommand(GamemodeType gamemode) {
        this.gamemode = gamemode;
    }

    @Override
    protected GamemodeType getForcedGamemode() {
        return gamemode;
    }

    @Override
    protected boolean isAliasCommand() {
        return true;
    }
}
