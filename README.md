# Battleship
CS342 Project with Ares, David, Phil

## Join Game Workflow
#### Client Ares joins queue
1. **Ares:** Ares presses PvP on GuiClient
2. **Ares:** Client send AddToQueue to server
3. **Server:** Server picks up AddToQueue and adds Ares to queue
4. **Ares:** GuiClient switches to queue scene, calling Client.getQueue every X seconds
5. **Ares:** Client.getQueue sends GetQueue to server
6. **Server:** Server picks up GetQueue, sends GetQueue back to client with current queue
7. **Ares:** GuiClient updates visual queue list via Client.getQueue callback...
8. **Ares:** GuiClient setups up Client.onInvite with callback function
9. **Ares:** GuiClient setups up Client.onAccept with callback function
#### Client David joins the queue
Same steps as client Ares joining the queue
#### Clients Ares & David join game
1. **Ares:** Ares clicks on Davids name on GuiClient
2. **Ares:** GuiClient calls Client.sendInvite with callback function
3. **Ares:** Client.sendInvite sends SendInvite object to server
4. **Server:** Server receives SendInvite, sends SendInvite to Davids' client
5. **David:** David Client.onInvite picks up SendInvite object, calling callback function
6. **David:** GuiClient displays Aress' invite
7. **David:** David presses Accept Invite on GuiClient
8. **David:** GuiClient calls Client.acceptInvite, which sends AcceptInvite object to server
9. **Server:** Server receives AcceptInvite, Ares is still in queue, so add David & Ares get added to a game
10. **Server:** Server sends AcceptInvite to Ares & David
11. **Ares:** Ares receives AcceptInvite, GuiClient initializes game
12. **David:** David receives AcceptInvite, GuiClient initializes game
## Play Game Workflow
### This assumes two players (David & Ares) have already joined the game
#### Aress' Turn
1. **David** Client.onOpponentShoot function awaiting Shoot object from server...
2. **Ares** Hovers over GuiClient on the tile to hit
3. **Ares** Clicks on tile, Client.shoot is called...
4. **Ares** Client.shoot sends clientMessage.Shoot to server
5. **Server** Picks up clientMessage.Shoot, and sends serverMessage.Shoot to the opponent with the data of Aress' shot (David)
6. **David** Client.onOpponentShootCallback is triggered, GuiClient inteprets serverMessage.Shoot object...
