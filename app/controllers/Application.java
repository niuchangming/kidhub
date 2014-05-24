package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;
import play.mvc.Http.Request;
import play.mvc.Scope.RouteArgs;
import play.templates.TemplateLoader;
import utils.CommonUtils;
import static models.User.Constant.*;

import java.util.*;

import enums.ArticleType;

import models.*;

@With(Interceptor.class)
public class Application extends Controller {

    public static void index() {
    	List<Article> articles = Article.find("order by created_date desc").fetch(20);
        render(articles);
    }
    
    public static void getArticlesByRange(int offset){
    	List<Article> articles = Article.find("order by created_date desc").from(offset).fetch(Article.DEFAULT_CCOUNT_PER_PAGE);
    	renderJSON(articles);
    }
    
}