package mffs.item.module.projector;

import mffs.api.IProjector;
import mffs.item.module.ItemModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class ItemModuleStablize extends ItemModule
{
	public ItemModuleStablize(int id)
	{
		super(id, "moduleStabilize");
		this.setMaxStackSize(1);
		this.setCost(10);
	}

	@Override
	public boolean onProject(IProjector projector, Vector3 position)
	{
		// Search nearby inventories to extract blocks.
		for (int dir = 0; dir < 6; dir++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(dir);
			TileEntity tileEntity = VectorHelper.getTileEntityFromSide(((TileEntity) projector).worldObj, new Vector3((TileEntity) projector), direction);

			if (tileEntity instanceof IInventory)
			{
				IInventory inventory = ((IInventory) tileEntity);

				for (int i = 0; i < inventory.getSizeInventory(); i++)
				{
					ItemStack checkStack = inventory.getStackInSlot(i);

					if (checkStack != null)
					{
						if (checkStack.getItem() instanceof ItemBlock)
						{
							try
							{
								((ItemBlock) checkStack.getItem()).placeBlockAt(checkStack, null, ((TileEntity) projector).worldObj, position.intX(), position.intY(), position.intZ(), 0, 0, 0, 0, checkStack.getItemDamage());
								inventory.decrStackSize(i, 1);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public float getFortronCost(int amplifier)
	{
		return super.getFortronCost(amplifier) * Math.max(Math.min((amplifier / 500), 1000), 1);
	}
}
