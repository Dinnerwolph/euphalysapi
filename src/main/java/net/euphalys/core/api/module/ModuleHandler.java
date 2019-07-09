package net.euphalys.core.api.module;

import lombok.Getter;
import net.euphalys.api.module.IModule;
import net.euphalys.api.module.IModuleHandler;
import net.euphalys.api.module.Module;
import net.euphalys.core.api.EuphalysApi;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author Dinnerwolph
 */
public class ModuleHandler implements IModuleHandler {

    private HashMap<String, ModuleInfo> library;
    private HashMap<String, ModuleInfo> registered;
    private HashMap<String, ModuleInfo> enabled;
    private Logger log;

    public ModuleHandler() {
        this.library = new HashMap<>();
        this.registered = new HashMap<>();
        this.enabled = new HashMap<>();
        this.log = EuphalysApi.getInstance().getLogger();
    }

    private void register(HashMap<String, ModuleInfo> map, IModule module) {
        if (module.getClass().isAnnotationPresent(Module.class)) {
            Module annotation = module.getClass().getAnnotation(Module.class);
            ModuleInfo moduleInfo = new ModuleInfo(annotation.id(), annotation.dependencies(), module);
            map.put(moduleInfo.id, moduleInfo);
        } else {
            log.warning("Unable to register IModule : " + module.getClass().getCanonicalName() + ". No Module annotation defined.");
        }
    }

    @Override
    public void register(IModule module) {
        register(registered, module);
    }

    @Override
    public void register(String moduleTag) {
        if (library.containsKey(moduleTag)) {
            registered.put(moduleTag, library.get(moduleTag));
        } else {
            log.warning("Unable to register module with tag " + moduleTag + ". Not found in module library.");
        }
    }

    @Override
    public void registerInLibrary(IModule module) {
        register(library, module);
    }

    public void enableModule(IModule module) {
        log.info("Enabling module : " + module.getClass().getSimpleName());
        module.onEnable();
    }

    @Override
    public void enableModuleRegistered(String moduleId) {
        this.enableModule(moduleId);
    }

    private void enableModule(String moduleId) {
        ModuleInfo moduleInfo = registered.get(moduleId);

        if (moduleInfo == null)
            moduleInfo = library.get(moduleId);

        if (moduleInfo != null) {
            if (!enabled.containsKey(moduleId)) {
                for (String dependency : moduleInfo.dependencies) {
                    enableModule(dependency);
                }

                enableModule(moduleInfo.module);
                this.enabled.put(moduleId, moduleInfo);
            }

        } else {
            log.severe("Unable to found module id : " + moduleId + ".");
        }
    }

    @Override
    public void enableAllModules() {
        for (String moduleId : this.registered.keySet()) {
            enableModule(moduleId);
        }
    }

    @Override
    public void disableAllModules() {
        for (ModuleInfo value : registered.values()) {
            value.module.onDisable();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IModule> T getModule(String moduleId) {
        ModuleInfo moduleInfo = enabled.get(moduleId);

        if (moduleInfo == null) {
            log.severe("Trying to get " + moduleId + " module : Not found or not enable");
            return null;
        }
        return (T) moduleInfo.module;
    }

    private class ModuleInfo {
        @Getter
        private String id;
        @Getter
        private String[] dependencies;
        @Getter
        private IModule module;

        ModuleInfo(String id, String[] dependencies, IModule module) {
            this.id = id;
            this.dependencies = dependencies;
            this.module = module;
        }
    }
}
