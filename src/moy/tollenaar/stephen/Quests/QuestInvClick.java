package moy.tollenaar.stephen.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import moy.tollenaar.stephen.CEvents.QuestProcessEvent;
import moy.tollenaar.stephen.CEvents.QuestProgEvent;
import moy.tollenaar.stephen.InventoryUtils.InventoryType;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.NPC.NPC;
import moy.tollenaar.stephen.NPC.NPCHandler;
import moy.tollenaar.stephen.Travel.Travel;

public class QuestInvClick implements Listener {

	private Travel tr;
	protected QuestsServerSide questers;
	private MoY plugin;

	public QuestInvClick(MoY instance) {
		this.tr = instance.tr;
		this.questers = instance.qserver;
		this.plugin = instance;
	}

	@EventHandler
	public void StaffQuestSelector(InventoryClickEvent event) {
		Inventory clickinv = event.getClickedInventory();
		if (event.getCurrentItem() != null
				&& event.getCurrentItem().getType() != Material.AIR) {

			if (clickinv != null
					&& (InventoryType.contains(clickinv.getName()))) {
				UUID npcuuid;
				try {
					// this is the uuid from the quest menu
					npcuuid = UUID.fromString(clickinv.getItem(0).getItemMeta()
							.getLore().get(3).replace("�", "").trim());
				} catch (NullPointerException | IllegalArgumentException
						| IndexOutOfBoundsException ex) {
					// this is the uuid from the main menu
					npcuuid = UUID.fromString(clickinv
							.getItem(clickinv.getSize() - 1).getItemMeta()
							.getLore().get(0));
				}

				ItemStack item = event.getCurrentItem();
				String name = item.getItemMeta().getDisplayName();
				Player player = (Player) event.getWhoClicked();
				if (clickinv.getName().equals("Main settings")) {
					if (name.equals("Kill Quests")) {
						questers.allkill(player,
								questers.GetIds("kill", npcuuid), npcuuid);
					} else if (name.equals("Harvest Quests")) {
						questers.allharvest(player,
								questers.GetIds("harvest", npcuuid), npcuuid);
					} else if (name.equals("Warp Lists")) {
						questers.allwarps(player, questers.getWarpId(npcuuid),
								npcuuid);
					} else if (name.equals("Talk to Quest")) {
						questers.alltalkto(player,
								questers.GetIds("talkto", npcuuid), npcuuid);
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("Active/Passive")) {
						if (questers.activenpc.get(npcuuid) != null) {
							event.setCancelled(true);
							player.closeInventory();
							questers.stop(npcuuid);
							questers.npcsettingsmain(npcuuid, player);

						} else {
							event.setCancelled(true);
							player.closeInventory();
							questers.runpoints(npcuuid);
							questers.npcsettingsmain(npcuuid, player);
						}
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("NPC name")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type the new npc name");
						ArrayList<String> t = new ArrayList<String>();
						t.add("Main");
						t.add(npcuuid.toString());
						questers.npcpos.put(player.getUniqueId(), t);

					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("Delete NPC")) {
						questers.despawnNPC(npcuuid);
						event.setCancelled(true);
						player.closeInventory();
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("NPC skinName")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type the new npc skin name");
						ArrayList<String> t = new ArrayList<String>();
						t.add("Skin");
						t.add(npcuuid.toString());
						questers.npcpos.put(player.getUniqueId(), t);
					} else if (event.getCurrentItem().getItemMeta()
							.getDisplayName().equals("Event Quest")) {
						questers.AllEvents(player,
								questers.GetIds("event", npcuuid), npcuuid);
					}
				} else if (clickinv.getName().equals("AllKill")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						int number = questers.createnewkill();
						questers.addKillquest(npcuuid, number);
						questers.returnkill(number).npcsettingskill(npcuuid,
								player);
					} else {
						event.setCancelled(true);
						questers.returnkill(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(2).replace("�", "").trim()))
								.npcsettingskill(npcuuid, player);

					}
				} else if (clickinv.getName().equals("AllHarvest")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						int n = questers.createnewhar();
						questers.addHarvestquest(npcuuid, n);
						questers.returnharvest(n).qinventory(player, npcuuid);
					} else {
						event.setCancelled(true);
						questers.returnharvest(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(2).replace("�", "").trim()))
								.qinventory(player, npcuuid);
					}
				} else if (clickinv.getName().equals("AllTalkTo")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						int n = questers.createnewtalk();
						questers.addTalktoquest(npcuuid, n);
						questers.returntalkto(n).npcsettingstalkto(npcuuid,
								player);
					} else {
						event.setCancelled(true);
						questers.returntalkto(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(2).replace("�", "").trim()))
								.npcsettingstalkto(npcuuid, player);
					}
				} else if (clickinv.getName().equals("AllWarps")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						int n = questers.createnewwarp(player.getLocation());

						questers.addWarp(npcuuid, n);
						questers.returnwarp(n).npcsettingswarplists(npcuuid,
								player);
					} else {
						event.setCancelled(true);
						questers.returnwarp(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(2).replace("�", "").trim()))
								.npcsettingswarplists(npcuuid, player);
					}
				} else if (clickinv.getName().equals("AllEvents")) {
					if (name.equals("Create New")) {
						event.setCancelled(true);
						int n = questers.createnewevent();
						questers.addEventquest(npcuuid, n);
						questers.returneventquest(n).openinv(player, npcuuid);
					} else {
						event.setCancelled(true);
						questers.returneventquest(
								Integer.parseInt(item.getItemMeta().getLore()
										.get(2).replace("�", "").trim()))
								.openinv(player, npcuuid);
					}
				}

				else if (!clickinv.getName().equals("WarpList")) {
					String type = clickinv.getItem(0).getItemMeta().getLore()
							.get(1).replace("�", "").trim();

					String questnumber = clickinv.getItem(0).getItemMeta()
							.getLore().get(2).replace("�", "").trim();

					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this quest");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Mob")) {
						event.setCancelled(true);
						player.closeInventory();
						if (type.equals("1")) {
							player.sendMessage("type the monster name");
						} else {
							player.sendMessage("type the monster name or type the item id");
						}
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						if (!type.equals("7")) {
							temp.add("mob");
						} else {
							temp.add("thing");
						}
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Count")) {
						event.setCancelled(true);
						player.closeInventory();
						if (type.equals("1")) {
							player.sendMessage("type how many monsters you need to kill");
						} else if (type.equals("2")) {
							player.sendMessage("type how many items you need to collect");
						} else if (type.equals("7")) {
							if (clickinv.getItem(1).getItemMeta()
									.getDisplayName().equals("Mob")) {
								player.sendMessage("type how many monsters you need to kill");
							} else {
								player.sendMessage("type how many items you need to collect");
							}
						}
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("count");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Reward")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the command withouth the /. use player for the players who will use it. if this is another reward use as first word add. if"
								+ "it is replacing a reward set the line number counting from 0 in the purple texts.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("reward");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Repeat Delay")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the repeat delay. If there isn't any repeat possible type -1");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("repeat");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Minimum Lvl")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for the minimum lvl of the player (in score board not xp lvl).");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("minimum");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Message")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the sentence the player would read when selecting the quest.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("message");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Prerequisite")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("prere if nothing is required prior to this type none. otherwise type: <killquest/harvestquest/talktoquest> <questnumber>");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("prereq");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Active Quest")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type true if the quest can be active otherwise type false");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("state");
						questers.npcpos.put(player.getUniqueId(), temp);
					}

					if (item.getItemMeta().getDisplayName()
							.equals("Delete Quest")) {
						event.setCancelled(true);

						switch (Integer.parseInt(type)) {
						case 1:
							questers.RemoveQuest(type, npcuuid,
									Integer.parseInt(questnumber));
							questers.removekill(Integer.parseInt(questnumber));
							questers.allkill(player,
									questers.GetIds(type, npcuuid), npcuuid);
							break;
						case 2:
							questers.RemoveQuest(type, npcuuid,
									Integer.parseInt(questnumber));
							questers.removeharvest(Integer
									.parseInt(questnumber));
							questers.allharvest(player,
									questers.GetIds(type, npcuuid), npcuuid);

							break;
						case 3:
							questers.RemoveQuest(type, npcuuid,
									Integer.parseInt(questnumber));
							questers.removetalkto(Integer.parseInt(questnumber));
							questers.alltalkto(player,
									questers.GetIds(type, npcuuid), npcuuid);
							break;
						case 7:
							questers.RemoveQuest(type, npcuuid,
									Integer.parseInt(questnumber));
							questers.removeevent(Integer.parseInt(questnumber));
							questers.AllEvents(player,
									questers.GetIds(type, npcuuid), npcuuid);
							break;
						}

						player.closeInventory();

					}
					if (item.getItemMeta().getDisplayName()
							.equals("Item to get")) {
						event.setCancelled(true);
						player.closeInventory();
						if (type.equals("2")) {
							player.sendMessage("type the item id");
						} else {
							player.sendMessage("type the item id or type the monster name");
						}
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						if (!type.equals("7")) {
							temp.add("item");
						} else {
							temp.add("thing");
						}
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("Person")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the name of the npc.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("person");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Start Date")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the start date in dd/mm");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("start");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("End Date")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the end date in dd/mm");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("end");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
				}
				if (clickinv.getName().equals("WarpList")) {
					String questnumber = clickinv.getItem(0).getItemMeta()
							.getLore().get(2).replace("�", "").trim();
					String type = clickinv.getItem(0).getItemMeta().getLore()
							.get(1).replace("�", "").trim();
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName().equals("Title")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type the new title of this warplists");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("title");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName().equals("To Main")) {
						event.setCancelled(true);
						player.closeInventory();
						questers.npcsettingsmain(npcuuid, player);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Delete Warp")) {
						event.setCancelled(true);
						questers.RemoveQuest("warp", npcuuid, -1);
						player.closeInventory();
						questers.allwarps(player, questers.getWarpId(npcuuid),
								npcuuid);
					}

					if (item.getItemMeta().getDisplayName()
							.equals("Type of transportation")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. If this is another type use as first word add. if"
								+ "it is replacing a type set the line number counting from 0 in the purple texts.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("type");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Cost of Ride")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number for how much money the ride costs, if free type 0");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("cost");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if (item.getItemMeta().getDisplayName()
							.equals("Active Warp")) {
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("Type true if the warp can be active otherwise type false");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("state");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
					if(item.getItemMeta().getDisplayName().equals("Override")){
						event.setCancelled(true);
						player.closeInventory();
						player.sendMessage("type a number as in <number><s/m/h> for the duration of the ride. Type -1 for auto generation");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(type);
						temp.add(npcuuid.toString());
						temp.add(questnumber);
						temp.add("bypass");
						questers.npcpos.put(player.getUniqueId(), temp);
					}
				}

				if (!event.isCancelled()) {
					event.setCancelled(true);
				}
			} else if (clickinv.getName().equals("NPCNames")) {
				if (event.getCurrentItem() != null) {
					if (event.getCurrentItem().getItemMeta() != null) {
						if (event.getWhoClicked() != null) {
							Inventory npcinv = event.getInventory();
							Player player = (Player) event.getWhoClicked();
							UUID tuuid = UUID.fromString(event.getCurrentItem()
									.getItemMeta().getLore().get(0));
							NPCHandler handler = plugin.getNPCHandler();
							NPC npc = handler.getNPCByUUID(tuuid);
							UUID npcuid = npc.getUniqueId();
							UUID npcuuid = UUID.fromString(npcinv
									.getItem(npcinv.getSize() - 1)
									.getItemMeta().getLore().get(0));
							ItemStack item = event.getCurrentItem();
							int id = Integer.parseInt(item.getItemMeta()
									.getLore().get(1));
							int questn = Integer.parseInt(npcinv
									.getItem(npcinv.getSize() - 1)
									.getItemMeta().getLore().get(1));
							questers.targetnpcs.put(npcuid, id);
							questers.returntalkto(questn).setNpcid(id);
							questers.npcpos.remove(player.getUniqueId());
							player.closeInventory();
							questers.returntalkto(questn).npcsettingstalkto(
									npcuuid, player);
							event.setCancelled(true);
						}
					}
				}
				event.setCancelled(true);
			} else if (clickinv.getName().equals("Wait Location info")
					|| clickinv.getName().equals("Trip Location info")) {
				ItemStack item = event.getCurrentItem();
				String name = item.getItemMeta().getDisplayName();
				Player player = (Player) event.getWhoClicked();
				String id = event.getClickedInventory().getItem(0)
						.getItemMeta().getLore().get(0);
				if (clickinv.getName().equals("Trip Location info")) {
					if (name.equals("Trip Type")) {
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. type 1 type.");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("trip");
						temp.add("type");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
						event.setCancelled(true);
					} else if (name.equals("Trip Location")) {
						player.closeInventory();
						player.sendMessage("go to the loction and type save");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("trip");
						temp.add("location");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
						event.setCancelled(true);
					} else if (name.equals("Delete")) {
						player.closeInventory();
						tr.removetrip(tr.GetTrip(Integer.parseInt(id))
								.getType(), Integer.parseInt(id));
						event.setCancelled(true);
					}
				} else if (clickinv.getName().equals("Wait Location info")) {
					if (name.equals("Trip Type")) {
						player.closeInventory();
						player.sendMessage("boat, oxcart or dragoncoach. type 1 type");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harbor");
						temp.add("type");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
						event.setCancelled(true);
					} else if (name.equals("Trip Location")) {
						player.closeInventory();
						player.sendMessage("go to the loction and type save");
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("harbor");
						temp.add("location");
						temp.add(id);
						questers.npcpos.put(player.getUniqueId(), temp);
						event.setCancelled(true);
					} else if (name.equals("Delete")) {
						player.closeInventory();
						tr.removeharbor(tr.GetHarbor(Integer.parseInt(id))
								.getType(), Integer.parseInt(id));
						event.setCancelled(true);
					}
				}
			} else if (clickinv.getName().equals("AllHarbors")) {
				String number = event.getCurrentItem().getItemMeta().getLore()
						.get(2).replace("�", "").trim();
				tr.GetHarbor(Integer.parseInt(number)).information(
						(Player) event.getWhoClicked());
				event.setCancelled(true);
			} else if (clickinv.getName().equals("AllTrips")) {
				String number = event.getCurrentItem().getItemMeta().getLore()
						.get(2).replace("�", "").trim();
				tr.GetTrip(Integer.parseInt(number)).information(
						(Player) event.getWhoClicked());
				event.setCancelled(true);
			}

		}
	}

	@EventHandler
	public void PlayerQuestSelector(InventoryClickEvent event) {
		Inventory clickedinv = event.getClickedInventory();
		if (event.getCurrentItem() != null
				&& event.getCurrentItem().getType() != Material.AIR) {
			if (clickedinv != null
					&& clickedinv.getName().equals("Aviable Quests")) {

				Player player = (Player) event.getWhoClicked();
				ItemStack item = event.getCurrentItem();
				if (event.getSlot() == clickedinv.getSize() - 1) {
					event.setCancelled(true);
					return;
				}
				int number = Integer.parseInt(item.getItemMeta().getLore()
						.get(item.getItemMeta().getLore().size() - 1)
						.replace("�", ""));
				String quetstype = GetTypeQuest(item.getType());

				if (quetstype.equals("event")) {
					String t = item.getItemMeta().getLore().get(0);
					quetstype = t.replaceAll("�", "").trim();
				}
				UUID npcuuid = UUID.fromString(clickedinv
						.getItem(clickedinv.getSize() - 1).getItemMeta()
						.getLore().get(0).replaceAll("�", ""));
				/**
				 * this is for adding new acitve quests
				 */
				if (!quetstype.equals("warp")) {
					try {
						String message = ChatColor.BLUE
								+ "["
								+ ChatColor.BLUE
								+ plugin.getNPCHandler().getNPCByUUID(npcuuid)
										.getName() + ChatColor.BLUE + "] "
								+ ChatColor.GRAY
								+ getmessage(quetstype, number);
						player.sendMessage(message);
						plugin.speech.removeCache(player.getUniqueId(),
								quetstype, number);
						SpeechType.constructSpeech(player, quetstype, number,
								plugin.getNPCHandler().getNPCByUUID(npcuuid)
										.getName());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					player.closeInventory();

				} else {
					int number2 = Integer.parseInt(item.getItemMeta().getLore()
							.get(item.getItemMeta().getLore().size() - 2)
							.replace("�", ""));
					player.closeInventory();
					tr.boardcheck(npcuuid, number, player,
							questers.returnwarp(number2).getStartloc(), item
									.getItemMeta().getLore().get(1), number
									+ "-" + number2 + "-"
									+ item.getItemMeta().getLore().get(1));
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onQuestProcess(QuestProcessEvent event) {

		questers.AddActiveQuest(event.getPlayer(), event.getQuestnumber(),
				event.getType());
		if (!event.getType().equals("talkto")) {
			if (questers.getProgress(event.getPlayer().getUniqueId()) != null) {
				if (questers.getProgress(event.getPlayer().getUniqueId()).get(
						event.getType()) != null) {
					questers.getProgress(event.getPlayer().getUniqueId())
							.get(event.getType())
							.put(event.getQuestnumber(), 0);
				} else {
					HashMap<Integer, Integer> numberq = new HashMap<Integer, Integer>();
					numberq.put(event.getQuestnumber(), 0);
					questers.getProgress(event.getPlayer().getUniqueId()).put(
							event.getType(), numberq);
				}
			} else {
				questers.addProgress(event.getPlayer().getUniqueId(),
						event.getType(), event.getQuestnumber());
			}
			if (event.getType().equals("harvest")) {
				for (ItemStack item : event.getPlayer().getInventory()
						.getContents()) {
					if (item != null && item.getType() != Material.AIR) {
						QuestHarvest harvest = questers.returnharvest(event
								.getQuestnumber());
						if (harvest.getItemId().split(":")[0].equals(Integer.toString(item
								.getTypeId()))) {
							if (harvest.getItemId().split(":").length != 1) {
								if (!harvest.getItemId().split(":")[1]
										.equals(Short.toString(item.getDurability()))) {
									continue;
								}
							}
							QuestProgEvent e = new QuestProgEvent(
									event.getPlayer(), event.getQuestnumber(),
									"harvest", item);
							Bukkit.getPluginManager().callEvent(e);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void QuestBlockers(InventoryClickEvent event) {
		Inventory clickedinv = event.getClickedInventory();
		if (clickedinv != null) {
			if (clickedinv.getName().startsWith("Rewarded")
					|| clickedinv.getName().startsWith("Completed")
					|| clickedinv.getName().startsWith("Active")) {
				event.setCancelled(true);
			}
		}
	}

	private String GetTypeQuest(Material itemtype) {
		switch (itemtype) {
		case DIAMOND_SWORD:
			return "kill";
		case DIAMOND_PICKAXE:
			return "harvest";
		case FEATHER:
			return "talkto";
		case ENCHANTED_BOOK:
			return "event";
		default:
			return "warp";
		}
	}

	private String getmessage(String type, int number) {
		switch (type) {
		case "kill":
			return questers.returnkill(number).getMessage();
		case "harvest":
			return questers.returnharvest(number).getMessage();
		case "talkto":
			return questers.returntalkto(number).getMessage();
		case "event":
			return questers.returneventquest(number).getMessage();
		default:
			return null;
		}
	}
}
