package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneralTest {

	@Test
	public void testWrapIndex() {
		
		for(int i=-101;i<=201;i++){
			for(int j = 3;j<=100;j++){
				int res = General.wrapIndex(i, j);
				int next = res+1;
				int nextactual = General.wrapIndex(i+1, j);
				if(next>=j){
					next=0;
				}
				if(next<0){
					next = j-1;
				}
				if(next!=nextactual){
					fail("Expected: "+next+". Parameters: "+(i+1)+", "+j+". Got: "+nextactual);
				}
			}
		}
	}

}
