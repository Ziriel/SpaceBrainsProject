package com.spacebrains.core.dao;

import com.spacebrains.core.rest.RESTApiProvider;
import com.spacebrains.model.Site;

import java.util.*;

/**
 * Обеспечивает хранение и обработку данных о сайтах,
 * полученных от RESTful web-сервиса
 */
public class SiteRepository {
    private ArrayList<Site> sites;
    private RESTApiProvider rest;

    public SiteRepository() {
        sites = new ArrayList<>();
        rest = new RESTApiProvider();
    }

    /**
     * Сохраняет изменения в списке сайтов путём запроса к REST API
     * @param site - сайт
     * @return true в случае удачного сохранения
     */
    public boolean put(Site site) {
        return rest.updateObject(new SiteDao(site));
    }

    /**
     * Запрашивает список сайтов, обращаясь к REST API
     * @return  ArrayList<Site>
     */
    public ArrayList<Site> get() {
        sites = new ArrayList<>();
        HashMap<Long, String> fromRest = rest.getObjects("sites");
        String value;
        for (long i: fromRest.keySet()) {
            value = fromRest.get(i);
            Site site = new Site((int) i, value);
            sites.add(site);
        }
        return sites;
    }

    /**
     * Удаляет сайт из списка с помощью запроса к REST API
     * @param site - сайт
     * @return true в случае удачного удаления
     */
    public boolean delete(Site site) {
        return rest.deleteObject(new SiteDao(site));
    }

    /**
     * Возвращает количество полученных записей
     * @return int
     */
    public int size() {
        return sites.size();
    }

    public Iterator<Site> iterator() {
        return sites.iterator();
    }

    /**
     * Внутренний класс-обёртка для объектов DbObject
     * позволяет использовать унифицированные вызовы при обращении к REST
     */
    private static class SiteDao extends DbObject {

        SiteDao(String jsonString) {
            super();
            addProperty("id", 0);
            addProperty("name",null);
            buildFromJSONString(jsonString);
        }

        SiteDao(Site site) {
            super();
            addProperty("id", site.getID());
            addProperty("name", site.getName());
        }

        @Override
        public String getEntityName() {
            return "site";
        }
    }
}
