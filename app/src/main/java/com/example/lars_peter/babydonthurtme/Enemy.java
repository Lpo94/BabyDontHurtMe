package com.example.lars_peter.babydonthurtme;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Lars-Peter on 15/03/2017.
 */

public class Enemy {
    private int[] memory;

    private boolean shipHit,foundBot,gotDirection,foundTop,shotFired = false;
    private int shipStart,lastHit,value,checkedDirections,difficulty, okay;
    private Context c;

    String message = "default message";
    Toast toast;

    ImageView current;
    Random r = new Random();


    public Enemy(int _difficulty, Context _c)
    {
        memory = new int[100];
        difficulty = _difficulty;
        c = _c;
    }

    // 0 1 2 3 4 5 6 7 8 9
    // 10
    public boolean EnemyTurn(GridView gridView)
    {
        shotFired = false;
        value = 0;
        // Set Bot to Right Difficulty
        switch(difficulty) {
            // Easy
            case 0:
                while (current == null) {
                    value = r.nextInt(99);
                    if(memory[value] == 0)
                    {
                        memory[value] = 1;
                        break;
                    }
                    current = null;
                }
                TakeShot(value,gridView);
                shotFired = true;
                break;
            // Can do Something
            case 1:
            if (!shipHit) {
                while (current == null && !shipHit) {
                    value = r.nextInt(99);
                    current = (ImageView) gridView.getChildAt(value);
                    if(memory[value] == 0)
                    {
                        memory[value] = 1;

                        break;
                    }
                    current = null;
                }

                if(TakeShot(value,gridView) && !shipHit)
                {
                    shipHit = true;
                    shipStart = value;
                    lastHit = shipStart;
                }
                shotFired = true;
                break;
            }
            else
            {


                if((!foundTop && !foundBot) || !foundTop || !foundBot) {
                    if (!gotDirection) {
                        switch (checkedDirections) {
                            case 0:
                                lastHit = lastHit - 10;
                                if (lastHit >= 0) {
                                    if (memory[lastHit] == 0) {
                                        memory[lastHit] = 1;
                                        if (TakeShot(lastHit, gridView)) {
                                            gotDirection = true;
                                            shotFired = true;
                                            break;
                                        } else {
                                            lastHit = shipStart;
                                            shotFired = true;
                                            checkedDirections++;
                                            break;
                                        }
                                    }

                                }
                                else {
                                    lastHit = shipStart;
                                    checkedDirections++;
                                }
                                break;
                            case 1:
                                lastHit = lastHit + 1;
                                if (lastHit == 10 || lastHit == 20 || lastHit == 30 || lastHit == 40
                                        || lastHit == 50 || lastHit == 60 || lastHit == 70 ||
                                        lastHit == 80 || lastHit == 90 || lastHit == 100) {
                                    foundTop = true;
                                    lastHit = shipStart;
                                    break;
                                } else {
                                    if (memory[lastHit] == 0) {
                                        memory[lastHit] = 1;
                                        if (TakeShot(lastHit, gridView)) {
                                            gotDirection = true;
                                            shotFired = true;
                                            break;
                                        } else {
                                            foundTop = true;
                                            lastHit = shipStart;
                                            shotFired = true;
                                            checkedDirections++;
                                            break;
                                        }
                                    } else {
                                        foundTop = true;
                                        checkedDirections++;
                                        lastHit = shipStart;
                                        break;
                                    }
                                }
                            case 2:
                                lastHit = lastHit + 10;
                                if (lastHit < 100) {
                                    if (memory[lastHit] == 0) {
                                        memory[lastHit] = 1;
                                        if (TakeShot(lastHit, gridView)) {
                                            shotFired = true;
                                            gotDirection = true;
                                            break;
                                        } else {
                                            lastHit = shipStart;
                                            shotFired = true;
                                            checkedDirections++;
                                            break;
                                        }
                                    }
                                } else {
                                    lastHit = shipStart;
                                    checkedDirections++;
                                }
                                break;
                            case 3:
                                if (lastHit - 1 > 0) {
                                    lastHit = lastHit - 1;
                                    if (lastHit == 9 || lastHit == 19 || lastHit == 29 || lastHit == 39
                                            || lastHit == 49 || lastHit == 59 || lastHit == 69 ||
                                            lastHit == 79 || lastHit == 89)  {
                                        foundBot = true;
                                        break;
                                    } else {
                                        if (memory[lastHit] == 0) {
                                            memory[lastHit] = 1;
                                            if (TakeShot(lastHit, gridView)) {
                                                gotDirection = true;
                                                shotFired = true;
                                                break;
                                            } else {
                                                foundBot = true;
                                                shotFired = true;
                                                break;
                                            }
                                        } else {
                                            foundBot = true;
                                            break;
                                        }
                                    }
                                } else {
                                    foundBot = true;
                                    break;
                                }


                        }
                    } else
                    if(!foundTop && !shotFired)
                    {
                        if(checkedDirections == 0) {
                            lastHit = lastHit - 10;
                            if (lastHit < 0) {
                                foundTop = true;
                                break;
                            } else {
                                if (memory[lastHit] == 0) {
                                    memory[lastHit] = 1;
                                    if (TakeShot(lastHit, gridView)) {
                                        shotFired = true;
                                        break;
                                    } else {
                                        foundTop = true;
                                        shotFired = true;
                                        break;
                                    }
                                } else {
                                    foundTop = true;
                                    checkedDirections++;
                                    lastHit = shipStart;
                                    break;
                                }
                            }
                        }
                        else if(checkedDirections == 1)
                        {
                            lastHit = lastHit + 1;
                            if (lastHit == 10 || lastHit == 20 || lastHit == 30 || lastHit == 40
                                    || lastHit == 50 || lastHit == 60 || lastHit == 70 ||
                                    lastHit == 80 || lastHit == 90 || lastHit == 100) {
                                foundTop = true;
                                checkedDirections++;
                                lastHit = shipStart;
                                break;
                            } else {
                                if (memory[lastHit] == 0) {
                                    memory[lastHit] = 1;
                                    if (TakeShot(lastHit, gridView)) {
                                        shotFired = true;
                                        break;
                                    } else {
                                        foundTop = true;
                                        shotFired = true;
                                        break;
                                    }
                                } else {
                                    foundTop = true;
                                    lastHit = shipStart;
                                    break;
                                }
                            }
                        }
                    }
                    if(!foundBot && foundTop && !shotFired) {
                        if (checkedDirections == 2 || checkedDirections == 0) {
                            lastHit = lastHit + 10;
                            if (lastHit < 100) {
                                if (memory[lastHit] == 0) {
                                    memory[lastHit] = 1;
                                    if (TakeShot(lastHit, gridView)) {
                                        shotFired = true;
                                        gotDirection = true;
                                        break;
                                    } else {
                                        shotFired = true;
                                        foundBot = true;
                                        break;
                                    }
                                }
                            } else {
                                foundBot = true;
                            }

                        }
                        else if(checkedDirections == 3 || checkedDirections == 1)
                        {
                            if (lastHit - 1 > 0) {
                                lastHit = lastHit - 1;
                                if (lastHit == 9 || lastHit == 19 || lastHit == 29 || lastHit == 39
                                        || lastHit == 49 || lastHit == 59 || lastHit == 69 ||
                                        lastHit == 79 || lastHit == 89)  {
                                    foundBot = true;
                                    break;
                                } else {
                                    if (memory[lastHit] == 0) {
                                        memory[lastHit] = 1;
                                        if (TakeShot(lastHit, gridView)) {
                                            shotFired = true;
                                            gotDirection = true;
                                            break;
                                        } else {
                                            foundBot = true;
                                            shotFired = true;
                                            break;
                                        }
                                    } else {
                                        foundBot = true;
                                        break;
                                    }
                                }
                            } else {
                                foundBot = true;
                                break;
                            }
                        }
                    }
                }
                else
                {
                    shipHit = false;
                    foundBot = false;
                    gotDirection = false;
                    foundTop = false;
                    current = null;
                    shipStart = 0;
                    lastHit = 0;
                    value = 0;
                    checkedDirections = 0;
                    passToast(c, "Kill ship");

                }
            }

                }

        okay++;
        passToast(c, "Message call count; "+String.valueOf(okay)+ " Position shot at; " + String.valueOf(lastHit)+ " Shot fired; "+ String.valueOf(shotFired)+ " foundBot; "+ String.valueOf(foundBot)+ " foundTop; "+ String.valueOf(foundTop)+" GotDirection: "+ String.valueOf(gotDirection)+ " CheckDirection: "+ String.valueOf(checkedDirections));


        current = null;
        return shotFired;
    }

    private boolean TakeShot(int _pos, GridView _gridView)
    {
        ImageView temp = (ImageView)_gridView.getChildAt(_pos);
        if((int)temp.getTag() == R.drawable.a1)
        {
            temp.setTag(R.drawable.c1);
            temp.setImageResource(R.drawable.c1);
            return false;
        }
        else if((int)temp.getTag() == R.drawable.d1)
        {
            temp.setTag(R.drawable.b1);
            temp.setImageResource(R.drawable.b1);
            return true;
        }

        return false;
    }

    private void passToast(Context context, String message)
    {
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }


}
