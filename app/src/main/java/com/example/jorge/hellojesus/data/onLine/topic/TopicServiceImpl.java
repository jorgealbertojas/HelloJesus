package com.example.jorge.hellojesus.data.onLine.topic;

import com.example.jorge.hellojesus.data.onLine.topic.model.ListTopic;
import com.example.jorge.hellojesus.data.onLine.topic.model.Topic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jorge on 21/02/2018.
 * Service for implement Api support with retrofit
 */

public class TopicServiceImpl implements TopicServiceApi {
    TopicEndPoint mRetrofit;

    public TopicServiceImpl(){
        mRetrofit = TopicClient.getClient().create(TopicEndPoint.class);
    }

    @Override
    public void getTopics(final TopicServiceCallback<ListTopic<Topic>> callback) {
        Call<ListTopic<Topic>> callTopic = mRetrofit.getTopic();
        callTopic.enqueue(new Callback<ListTopic<Topic>>() {
            @Override
            public void onResponse(Call<ListTopic<Topic>> call, Response<ListTopic<Topic>> response) {
                if(response.code()==200){
                    ListTopic<Topic> resultSearch = response.body();
                    ListTopic<Topic> TempResultSearch = response.body();


                    List<Topic> topicList =  new ArrayList<Topic>();


                    for (int i = 0; i < resultSearch.items.size(); i++){
                        if (resultSearch.items.get(i).getType().toString().equals("insert")){


                            resultSearch.items.get(i).setType("music");

                            topicList =  new ArrayList<Topic>();
                            Topic topic =  new Topic();
                            topic.setContent(resultSearch.items.get(i).getContent());
                            topic.setId(resultSearch.items.get(i).getId());
                            topic.setName("Sing");
                            topic.setAudio("sing");
                            topic.setType("music");
                            topic.setYoutube(resultSearch.items.get(i).getYoutube());
                            topic.setGlossary(resultSearch.items.get(i).getGlossary());
                            topic.setTime(resultSearch.items.get(i).getTime());
                            topicList.add(topic);
                            TempResultSearch.items.addAll(topicList);

                            topicList =  new ArrayList<Topic>();
                            topic =  new Topic();
                            topic.setContent(resultSearch.items.get(i).getContent());
                            topic.setId(resultSearch.items.get(i).getId());
                            topic.setName("Translate");
                            topic.setAudio(resultSearch.items.get(i).getAudio());
                            topic.setType("music");
                            topic.setYoutube(resultSearch.items.get(i).getYoutube());
                            topic.setGlossary(resultSearch.items.get(i).getGlossary());
                            topic.setTime(resultSearch.items.get(i).getTime());
                            topicList.add(topic);
                            TempResultSearch.items.addAll(topicList);

                            topicList =  new ArrayList<Topic>();
                            topic =  new Topic();
                            topic.setContent(resultSearch.items.get(i).getContent());
                            topic.setId(resultSearch.items.get(i).getId());
                            topic.setName(resultSearch.items.get(i).getName());
                            topic.setAudio(resultSearch.items.get(i).getAudio());
                            topic.setType("exercise");
                            topic.setYoutube(resultSearch.items.get(i).getYoutube());
                            topic.setGlossary(resultSearch.items.get(i).getGlossary());
                            topic.setTime(resultSearch.items.get(i).getTime());
                            topicList.add(topic);
                            TempResultSearch.items.addAll(topicList);

                        }
                    }


                    callback.onLoaded(TempResultSearch);
                }
            }

            @Override
            public void onFailure(Call<ListTopic<Topic>> call, Throwable t) {

            }
        });
    }
}

