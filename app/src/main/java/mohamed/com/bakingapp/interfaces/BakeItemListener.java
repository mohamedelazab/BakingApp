package mohamed.com.bakingapp.interfaces;

import mohamed.com.bakingapp.model.BakedResponse;

public interface BakeItemListener {

    public void onBakeItemClicked(BakedResponse bakedResponse, int position);
}
